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
    public BranchEntity getBranchById(@PathVariable int id) {
        return branchService.getBranchById(id);
    }

    @PostMapping("")
    public ResponseEntity<String> addNewBranch(@RequestBody @Valid BranchDataDto branchData) {
        branchService.addNewBranch(branchData);
        return new ResponseEntity<>("Branch created", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBranchById(@PathVariable int id) {
        branchService.deleteBranchById(id);
        return new ResponseEntity<>("Branch deleted", HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateBranchById(@PathVariable int id, @RequestBody @Valid BranchDataDto branchData) {
        branchService.updateBranchById(id, branchData);
        return new ResponseEntity<>("Branch updated", HttpStatus.OK);
    }


}
