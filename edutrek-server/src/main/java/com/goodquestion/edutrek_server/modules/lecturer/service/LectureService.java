package com.goodquestion.edutrek_server.modules.lecturer.service;

import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseAddingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseDeletingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseUpdatingException;
import com.goodquestion.edutrek_server.error.ShareException.LecturerNotFoundException;
import com.goodquestion.edutrek_server.modules.lecturer.dto.LecturerDataDto;
import com.goodquestion.edutrek_server.modules.lecturer.dto.LecturerPaginationResponseDto;
import com.goodquestion.edutrek_server.modules.lecturer.persistence.LecturerArchiveEntity;
import com.goodquestion.edutrek_server.modules.lecturer.persistence.LecturerArchiveRepository;
import com.goodquestion.edutrek_server.modules.lecturer.persistence.LecturerEntity;
import com.goodquestion.edutrek_server.modules.lecturer.persistence.LecturerRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LecturerRepository repository;
    private final LecturerArchiveRepository archiveRepository;
    private final EntityManager entityManager;

    public List<LecturerEntity> getAll() {
        return repository.findAll();
    }

    public LecturerEntity getById(UUID id) {
        return repository.getLecturerById(id).or(() -> archiveRepository.getLecturerById(id)).orElseThrow(() -> new LecturerNotFoundException(id.toString()));
    }

    public LecturerPaginationResponseDto getAllPaginated(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<LecturerEntity> pageEntity = repository.findAll(pageable);
        return new LecturerPaginationResponseDto(pageEntity.getContent(), pageEntity.getTotalElements(), page, limit);
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
    public void deleteById(UUID id) {
        int deletedRows;
        try {
            deletedRows = repository.deleteLecturerById(id);
            if (deletedRows == 0)
                deletedRows = archiveRepository.deleteLecturerById(id);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }

        if (deletedRows == 0)
            throw new LecturerNotFoundException(String.valueOf(id));
    }

    @Transactional
    public void updateById(UUID id, LecturerDataDto data) {
        LecturerEntity entity = repository.getLecturerById(id).or(() -> archiveRepository.getLecturerById(id)).orElseThrow(() -> new LecturerNotFoundException(id.toString()));
        entity.setLecturerName(data.getLecturerName());
        entity.setPhone(data.getPhone());
        entity.setEmail(data.getEmail());
        entity.setBranchId(data.getBranchId());
        entity.setComment(data.getComment());
        try {
            repository.save(entity);
        } catch (Exception e) {
            throw new DatabaseUpdatingException(e.getMessage());
        }
    }

    @Transactional
    public void archiveById(UUID uuid, String reason) {
        LecturerEntity lecturer = repository.getLecturerById(uuid).orElseThrow(() -> new LecturerNotFoundException(uuid.toString()));
        LecturerArchiveEntity archiveLecturer = new LecturerArchiveEntity(lecturer, reason);
        try {
            repository.deleteLecturerById(uuid);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
        entityManager.detach(lecturer);
        try {
            archiveRepository.save(archiveLecturer);
        } catch (Exception e) {
            throw new DatabaseAddingException(e.getMessage());
        }
    }
}
