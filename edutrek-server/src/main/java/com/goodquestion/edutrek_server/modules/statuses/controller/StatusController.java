package com.goodquestion.edutrek_server.modules.statuses.controller;

import com.goodquestion.edutrek_server.modules.statuses.dto.StatusDataDto;
import com.goodquestion.edutrek_server.modules.statuses.persistence.StatusEntity;
import com.goodquestion.edutrek_server.modules.statuses.service.StatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/statuses")
@RequiredArgsConstructor
public class StatusController {

    private final StatusService statusService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<StatusEntity> getAllStatuses() {
        return statusService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StatusEntity getStatusById(@PathVariable int id) {
            return statusService.getById(id);
    }

    @PostMapping("")
    public ResponseEntity<String> addNewStatus(@RequestBody @Valid StatusDataDto statusData) {
        statusService.addEntity(statusData);
        return new ResponseEntity<>("Status created", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStatusById(@PathVariable int id) {
            statusService.deleteById(id);
            return new ResponseEntity<>("Status deleted", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateStatusById(@PathVariable int id, @RequestBody @Valid StatusDataDto statusData) {
            statusService.updateById(id, statusData.getStatusName());
            return new ResponseEntity<>("Status updated", HttpStatus.OK);
    }


}
