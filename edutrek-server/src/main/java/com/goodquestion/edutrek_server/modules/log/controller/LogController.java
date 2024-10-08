package com.goodquestion.edutrek_server.modules.log.controller;

import com.goodquestion.edutrek_server.modules.log.service.LogService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogService service;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getLogsById(@PathVariable @UUID String id) {
            return service.getById(java.util.UUID.fromString(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> addNewLogById(@PathVariable @UUID String id, @RequestBody String log) {
        service.add(java.util.UUID.fromString(id), log);
        return new ResponseEntity<>("Log added", HttpStatus.CREATED);
    }
}
