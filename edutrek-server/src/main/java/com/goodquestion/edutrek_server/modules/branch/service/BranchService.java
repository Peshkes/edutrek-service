package com.goodquestion.edutrek_server.modules.branch.service;

import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseAddingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseDeletingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseUpdatingException;
import com.goodquestion.edutrek_server.error.ShareException.BranchNotFoundException;
import com.goodquestion.edutrek_server.modules.branch.dto.BranchDataDto;
import com.goodquestion.edutrek_server.modules.branch.persistence.BranchEntity;
import com.goodquestion.edutrek_server.modules.branch.persistence.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository repository;

    public List<BranchEntity> getAll() {
        return repository.findAll();
    }

    public BranchEntity getById(int branchId) {
        return repository.findById(branchId).orElseThrow(() -> new BranchNotFoundException(String.valueOf(branchId)));
    }

    @Transactional
    public void addEntity(BranchDataDto branchData) {
        try {
            repository.save(new BranchEntity(branchData.getBranchName(), branchData.getBranchAddress()));
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
    public void updateById(int branchId, BranchDataDto branchData) {
        BranchEntity branchEntity = repository.findById(branchId).orElseThrow(() -> new BranchNotFoundException(String.valueOf(branchId)));
        branchEntity.setBranchName(branchData.getBranchName());
        branchEntity.setBranchAddress(branchData.getBranchAddress());
        try {
            repository.save(branchEntity);
        } catch (Exception e) {
            throw new DatabaseUpdatingException(e.getMessage());
        }
    }
}
