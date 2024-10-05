package com.goodquestion.edutrek_server.modules.lecturer.controller;

import com.goodquestion.edutrek_server.modules.lecturer.dto.LecturerDataDto;
import com.goodquestion.edutrek_server.modules.lecturer.persistence.LecturerEntity;
import com.goodquestion.edutrek_server.modules.lecturer.service.LectureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lecturers")
@RequiredArgsConstructor
public class LecturerController {

    private final LectureService service;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<LecturerEntity> getAllStatuses() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LecturerEntity getStatusById(@PathVariable @UUID String id) {
            return service.getById(java.util.UUID.fromString(id));
    }

    @PostMapping("")
    public ResponseEntity<String> addNewStatus(@RequestBody @Valid LecturerDataDto data) {
        service.addEntity(data);
        return new ResponseEntity<>("Lecturer created", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStatusById(@PathVariable @UUID String id) {
            service.deleteById(java.util.UUID.fromString(id));
            return new ResponseEntity<>("Lecturer deleted", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateStatusById(@PathVariable @UUID String id, @RequestBody @Valid LecturerDataDto data) {
            service.updateById(java.util.UUID.fromString(id), data);
            return new ResponseEntity<>("Lecturer updated", HttpStatus.OK);
    }
}
