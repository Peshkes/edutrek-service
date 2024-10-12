package com.goodquestion.edutrek_server.modules.branch.service;

import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseAddingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseDeletingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseUpdatingException;
import com.goodquestion.edutrek_server.error.ShareException.BranchNotFoundException;
import com.goodquestion.edutrek_server.modules.branch.dto.BranchDataDto;
import com.goodquestion.edutrek_server.modules.branch.persistence.BranchEntity;
import com.goodquestion.edutrek_server.modules.branch.persistence.BranchRepository;
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
@CacheConfig(cacheNames={"branches"})
public class BranchService {


    private final BranchRepository repository;

    @Cacheable(key = "#root.methodName")
    public List<BranchEntity> getAll() {
        return repository.findAll();
    }

    @Cacheable(key = "#id")
    public BranchEntity getById(int id) {
        return repository.findById(id).orElseThrow(() -> new BranchNotFoundException(String.valueOf(id)));
    }

    @CacheEvict(key = "{'getALl'}")
    @Transactional
    public void addEntity(BranchDataDto branchData) {
        try {
            repository.save(new BranchEntity(branchData.getBranchName(), branchData.getBranchAddress()));
        } catch (Exception e) {
            throw new DatabaseAddingException(e.getMessage());
        }
    }

    @Transactional
    @CacheEvict(key = "#id")
    public void deleteById(int id) {
        if (!repository.existsById(id)) throw new BranchNotFoundException(String.valueOf(id));

        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
    }

    @Transactional
    @CachePut(key = "#id")
    public void updateById(int id, BranchDataDto branchData) {
        BranchEntity branchEntity = repository.findById(id).orElseThrow(() -> new BranchNotFoundException(String.valueOf(id)));
        branchEntity.setBranchName(branchData.getBranchName());
        branchEntity.setBranchAddress(branchData.getBranchAddress());
        try {
            repository.save(branchEntity);
        } catch (Exception e) {
            throw new DatabaseUpdatingException(e.getMessage());
        }
    }
}
