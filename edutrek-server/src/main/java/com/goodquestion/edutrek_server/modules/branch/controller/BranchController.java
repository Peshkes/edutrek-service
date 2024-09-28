package com.goodquestion.edutrek_server.modules.branch.controller;

import com.goodquestion.edutrek_server.modules.branch.dto.BranchDataDto;
import com.goodquestion.edutrek_server.modules.branch.persistence.BranchEntity;
import com.goodquestion.edutrek_server.modules.branch.service.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<BranchEntity> getAllBranches() {
        return branchService.getAllBranches();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BranchEntity getBranchById(@PathVariable String id) {
        try {
            int branchId = Integer.parseInt(id);
            return branchService.getBranchById(branchId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID must be a number");
        }
    }

    @PostMapping("")
    public ResponseEntity<String> addNewBranch(@RequestBody @Valid BranchDataDto branchData) {
        branchService.addNewBranch(branchData);
        return new ResponseEntity<>("Branch created", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBranchById(@PathVariable String id) {
        try {
            int branchId = Integer.parseInt(id);
            branchService.deleteBranchById(branchId);
            return new ResponseEntity<>("Branch deleted", HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID must be a number");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBranchById(@PathVariable String id, @RequestBody @Valid BranchDataDto branchData) {
        try {
            int branchId = Integer.parseInt(id);
            branchService.updateBranchById(branchId, branchData);
            return new ResponseEntity<>("Branch updated", HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID must be a number");
        }
    }


}
