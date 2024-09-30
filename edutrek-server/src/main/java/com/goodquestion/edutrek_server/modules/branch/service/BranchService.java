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

    private final BranchRepository branchRepository;

    public List<BranchEntity> getAllBranches() {
        return branchRepository.findAll();
    }

    public BranchEntity getBranchById(int branchId) {
        return branchRepository.findById(branchId).orElseThrow(() -> new BranchNotFoundException(String.valueOf(branchId)));
    }

    @Transactional
    public void addNewBranch(BranchDataDto branchData) {
        try {
            branchRepository.save(new BranchEntity(branchData.getBranchName(), branchData.getBranchAddress()));
        } catch (Exception e) {
            throw new DatabaseAddingException(e.getMessage());
        }
    }

    @Transactional
    public void deleteBranchById(int branchId) {
        if (!branchRepository.existsById(branchId)) throw new BranchNotFoundException(String.valueOf(branchId));

        try {
            branchRepository.deleteById(branchId);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
    }

    @Transactional
    public void updateBranchById(int branchId, BranchDataDto branchData) {
        BranchEntity branchEntity = branchRepository.findById(branchId).orElseThrow(() -> new BranchNotFoundException(String.valueOf(branchId)));
        branchEntity.setBranchName(branchData.getBranchName());
        branchEntity.setBranchAddress(branchData.getBranchAddress());
        try {
            branchRepository.save(branchEntity);
        } catch (Exception e) {
            throw new DatabaseUpdatingException(e.getMessage());
        }
    }
}
