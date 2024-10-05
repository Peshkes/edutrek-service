package com.goodquestion.edutrek_server.modules.lecturer.service;

import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseAddingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseDeletingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseUpdatingException;
import com.goodquestion.edutrek_server.error.ShareException.LecturerNotFoundException;
import com.goodquestion.edutrek_server.modules.lecturer.dto.LecturerDataDto;
import com.goodquestion.edutrek_server.modules.lecturer.persistence.LecturerEntity;
import com.goodquestion.edutrek_server.modules.lecturer.persistence.LecturerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LecturerRepository repository;

    public List<LecturerEntity> getAll() {
        return repository.findAll();
    }

    public LecturerEntity getById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new LecturerNotFoundException(id.toString()));
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
        if (!repository.existsById(id)) throw new LecturerNotFoundException(String.valueOf(id));

        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
    }

    @Transactional
    public void updateById(UUID id, LecturerDataDto data) {
        LecturerEntity entity = repository.findById(id).orElseThrow(() -> new LecturerNotFoundException(id.toString()));
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
}
