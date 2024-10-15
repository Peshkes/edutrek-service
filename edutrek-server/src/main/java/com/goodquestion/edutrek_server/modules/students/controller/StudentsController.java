package com.goodquestion.edutrek_server.modules.students.controller;



import com.goodquestion.edutrek_server.modules.students.dto.StudentSearchDto;
import com.goodquestion.edutrek_server.modules.students.dto.StudentsAddDto;
import com.goodquestion.edutrek_server.modules.students.dto.StudentsInfoDataFromContactDto;

import com.goodquestion.edutrek_server.modules.students.persistence.current.StudentEntity;

import com.goodquestion.edutrek_server.modules.students.service.StudentsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentsController {

    private final StudentsService studentsInfoService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public StudentSearchDto getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer statusId,
            @RequestParam(required = false) Integer groupId) {
        return studentsInfoService.getAll(page,pageSize,search,statusId,groupId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentEntity getById(@PathVariable UUID id) {
        return studentsInfoService.getById(id);
    }

    @PostMapping("")
    public ResponseEntity<String> addEntity(@RequestBody @Valid StudentsAddDto studentsAddDto) {
        studentsInfoService.addEntity(studentsAddDto.contactData(), studentsAddDto.studentsInfoData());
        return new ResponseEntity<>("Student created", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable UUID id) {
        studentsInfoService.deleteById(id);
        return new ResponseEntity<>("Student deleted", HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateById(@PathVariable UUID id, @RequestBody @Valid StudentsInfoDataFromContactDto contactData) {
        studentsInfoService.updateById(id, contactData);
        return new ResponseEntity<>("Student updated", HttpStatus.OK);
    }

    @PutMapping("/archive/{id}/{reason}")
    public ResponseEntity<String> moveToArchiveById(@PathVariable UUID id,@PathVariable @DefaultValue("") String reason) {
        studentsInfoService.moveToArchiveById(id, reason);
        return new ResponseEntity<>("Student moved to archive", HttpStatus.OK);
    }

    @PutMapping("/graduate/{id}")
    public ResponseEntity<String> graduateById(@PathVariable UUID id) {
        studentsInfoService.graduateById(id);
        return new ResponseEntity<>("Student moved to archive", HttpStatus.OK);
    }


}
