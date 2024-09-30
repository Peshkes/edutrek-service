package com.goodquestion.edutrek_server.modules.statuses.service;

import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseAddingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseDeletingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseUpdatingException;

import static com.goodquestion.edutrek_server.error.ShareException.*;


import com.goodquestion.edutrek_server.modules.statuses.dto.StatusDataDto;
import com.goodquestion.edutrek_server.modules.statuses.persistence.StatusEntity;
import com.goodquestion.edutrek_server.modules.statuses.persistence.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class StatusService {

    private final StatusRepository repository;

    public List<StatusEntity> getAll() {
        return repository.findAll();
    }

    public StatusEntity getById(int statusId) {
        return repository.findById(statusId).orElseThrow(() -> new StatusNotFoundException(statusId));
    }

    @Transactional
    public void addEntity(StatusDataDto statusData) {
        try {
            repository.save(new StatusEntity(statusData.getStatusName()));
        } catch (Exception e) {
            throw new DatabaseAddingException(e.getMessage());
        }
    }

    @Transactional
    public void deleteById(int branchId) {
        if (!repository.existsById(branchId)) throw new BranchNotFoundException(String.valueOf(branchId));

        try {
            repository.deleteById(branchId);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
    }

    @Transactional
    public void updateById(int statusId, String newName) { //заменил на стринг
        StatusEntity status = repository.findById(statusId).orElseThrow(() -> new StatusNotFoundException(statusId));
        status.setStatusName(newName);
        try {
            repository.save(status);
        } catch (Exception e) {
            throw new DatabaseUpdatingException(e.getMessage());
        }
    }
}
