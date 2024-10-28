package com.goodquestion.edutrek_server.modules.statuses.service;

import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseAddingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseDeletingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseUpdatingException;

import static com.goodquestion.edutrek_server.error.ShareException.*;


import com.goodquestion.edutrek_server.modules.statuses.dto.StatusDataDto;
import com.goodquestion.edutrek_server.modules.statuses.persistence.StatusEntity;
import com.goodquestion.edutrek_server.modules.statuses.persistence.StatusRepository;
import com.goodquestion.edutrek_server.utility_service.logging.Loggable;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames={"statuses"})
public class StatusService {

    private final StatusRepository repository;

    @Loggable
    @Cacheable(key = "{'all'}")
    public List<StatusEntity> getAll() {
        return repository.findAll();
    }

    @Loggable
    @Cacheable(key = "#id")
    public StatusEntity getById(int id) {
        return repository.findById(id).orElseThrow(() -> new StatusNotFoundException(id));
    }

    @Loggable
    @Transactional
    @CacheEvict(key = "{'all'}")
    public void addEntity(StatusDataDto statusData) {
        try {
            repository.save(new StatusEntity(statusData.getStatusName()));
        } catch (Exception e) {
            throw new DatabaseAddingException(e.getMessage());
        }
    }

    @Loggable
    @Transactional
    @CachePut(key = "#id")
    public void deleteById(int id) {
        if (!repository.existsById(id)) throw new BranchNotFoundException(String.valueOf(id));

        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
    }

    @Loggable
    @Transactional
    @CachePut(key = "#id")
    public void updateById(int id, String newName) { //заменил на стринг
        StatusEntity status = repository.findById(id).orElseThrow(() -> new StatusNotFoundException(id));

        status.setStatusName(newName);
        try {
            repository.save(status);
        } catch (Exception e) {
            throw new DatabaseUpdatingException(e.getMessage());
        }
    }
}
