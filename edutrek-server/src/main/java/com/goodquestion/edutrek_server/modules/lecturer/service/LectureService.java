package com.goodquestion.edutrek_server.modules.lecturer.service;

import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseAddingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseDeletingException;
import com.goodquestion.edutrek_server.error.ShareException.LecturerNotFoundException;
import com.goodquestion.edutrek_server.modules.group.persistence.lecturers_by_group.BaseLecturerByGroup;
import com.goodquestion.edutrek_server.modules.group.persistence.lecturers_by_group.ILecturerByGroupRepository;
import com.goodquestion.edutrek_server.modules.group.persistence.lecturers_by_group.LecturersByGroupArchiveRepository;
import com.goodquestion.edutrek_server.modules.group.persistence.lecturers_by_group.LecturersByGroupRepository;
import com.goodquestion.edutrek_server.modules.lecturer.dto.LecturerDataDto;
import com.goodquestion.edutrek_server.modules.lecturer.dto.LecturerPaginationResponseDto;
import com.goodquestion.edutrek_server.modules.lecturer.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LecturerRepository repository;
    private final LecturerArchiveRepository archiveRepository;
    private final LecturersByGroupRepository lecturersByGroupRepository;
    private final LecturersByGroupArchiveRepository lecturersByGroupArchiveRepository;

    public BaseLecturer getById(UUID id) {
        return repository.getLecturerByLecturerId(id).or(() -> archiveRepository.getLecturerByLecturerId(id)).orElseThrow(() -> new LecturerNotFoundException(id.toString()));
    }

    public LecturerPaginationResponseDto getAllPaginated(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<LecturerEntity> mainPage = repository.findAll(pageable);
        List<BaseLecturer> results = new ArrayList<>(mainPage.getContent());

        if (mainPage.getTotalElements() < pageable.getPageSize()) {
            int remainingElements = pageable.getPageSize() - results.size();
            Pageable archivePageable = PageRequest.of(0, remainingElements);
            Page<LecturerArchiveEntity> archivePage = archiveRepository.findAll(archivePageable);
            results.addAll(archivePage.getContent());
        }

        long totalElements = repository.count() + archiveRepository.count();

        return new LecturerPaginationResponseDto(results, totalElements, pageable.getPageNumber(), pageable.getPageSize());
    }

    @Transactional
    public void addEntity(LecturerDataDto data) {
        try {
            repository.save(new LecturerEntity(data.getLecturerName(), data.getPhone(), data.getEmail(), data.getBranchId(), data.getComment()));
        } catch (Exception e) {
            throw new DatabaseAddingException(e.getMessage());
        }
    }

    @Transactional
    public BaseLecturer deleteById(UUID id) {
        BaseLecturer lecturer = deleteFromRepository(id, repository, lecturersByGroupRepository);

        if (lecturer == null) lecturer = deleteFromRepository(id, archiveRepository, lecturersByGroupArchiveRepository);
        if (lecturer == null) throw new LecturerNotFoundException(id.toString());

        return lecturer;
    }

    private <T extends BaseLecturer, U extends BaseLecturerByGroup> T deleteFromRepository(
            UUID id, ILecturerRepository<T> lecturerRepo, ILecturerByGroupRepository<U> lecturerByGroupRepo) {
        T entity = lecturerRepo.findById(id).orElse(null);
        if (entity != null) {
            List<U> lecturersByGroup = lecturerByGroupRepo.getByGroupId(id);
            try {
                if (!lecturersByGroup.isEmpty()) lecturerByGroupRepo.deleteAll(lecturersByGroup);
                lecturerRepo.deleteById(id);
            } catch (Exception e) {
                throw new DatabaseDeletingException(e.getMessage());
            }
        }
        return entity;
    }

    @Transactional
    public void updateById(UUID id, LecturerDataDto data) {
        BaseLecturer entity = repository.getLecturerByLecturerId(id).or(() -> archiveRepository.getLecturerByLecturerId(id)).orElseThrow(() -> new LecturerNotFoundException(id.toString()));
        entity.setLecturerName(data.getLecturerName());
        entity.setPhone(data.getPhone());
        entity.setEmail(data.getEmail());
        entity.setBranchId(data.getBranchId());
        entity.setComment(data.getComment());
    }


    @Transactional
    public void archiveById(UUID uuid, String reason) {
        if (repository.existsById(uuid)) {
            BaseLecturer lecturer = deleteById(uuid);
            try {
                archiveRepository.save(new LecturerArchiveEntity(lecturer, reason));
            } catch (Exception e) {
                throw new DatabaseAddingException(e.getMessage());
            }
        }
    }
}
