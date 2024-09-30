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

    private final StatusRepository statusRepository;

    public List<StatusEntity> getAllStatuses() {
        return statusRepository.findAll();
    }

    public StatusEntity getStatusById(int statusId) {
        return statusRepository.findById(statusId).orElseThrow(() -> new StatusNotFoundException(statusId));
    }

    @Transactional
    public void addNewStatus(StatusDataDto statusData) {
        try {
            statusRepository.save(new StatusEntity(statusData.getStatusName()));
        } catch (Exception e) {
            throw new DatabaseAddingException(e.getMessage());
        }
    }

    @Transactional
    public void deleteStatusById(int branchId) {
        if (!statusRepository.existsById(branchId)) throw new BranchNotFoundException(String.valueOf(branchId));

        try {
            statusRepository.deleteById(branchId);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
    }

    @Transactional
    public void updateStatusById(int statusId, String newName) { //заменил на стринг
        StatusEntity status = statusRepository.findById(statusId).orElseThrow(() -> new StatusNotFoundException(statusId));
        status.setStatusName(newName);
        try {
            statusRepository.save(status);
        } catch (Exception e) {
            throw new DatabaseUpdatingException(e.getMessage());
        }
    }
}
