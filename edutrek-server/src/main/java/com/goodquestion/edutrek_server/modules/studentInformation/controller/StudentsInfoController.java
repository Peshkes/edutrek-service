package com.goodquestion.edutrek_server.modules.studentInformation.controller;



import com.goodquestion.edutrek_server.modules.studentInformation.dto.StudentInfoSearchDto;
import com.goodquestion.edutrek_server.modules.studentInformation.dto.StudentsInfoDataDto;

import com.goodquestion.edutrek_server.modules.studentInformation.persistence.StudentInfoEntity;

import com.goodquestion.edutrek_server.modules.studentInformation.service.StudentsInfoService;
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
public class StudentsInfoController {

    private final StudentsInfoService studentsInfoService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public StudentInfoSearchDto getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer statusId,
            @RequestParam(required = false) Integer groupId) {
        return studentsInfoService.getAll(page,pageSize,search,statusId,groupId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentInfoEntity getById(@PathVariable UUID id) {
        return studentsInfoService.getById(id);
    }

    @PostMapping("")
    public ResponseEntity<String> addEntity(@RequestBody @Valid StudentsInfoDataDto studentInfoData) {
        studentsInfoService.addEntity(studentInfoData);
        return new ResponseEntity<>("Student created", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable UUID id) {
        studentsInfoService.deleteById(id);
        return new ResponseEntity<>("Contact deleted", HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateById(@PathVariable UUID id, @RequestBody @Valid StudentsInfoDataDto contactData) {
        studentsInfoService.updateById(id, contactData);
        return new ResponseEntity<>("Contact updated", HttpStatus.OK);
    }

    @PutMapping("/archive/{id}/{reason}")
    public ResponseEntity<String> moveToArchiveById(@PathVariable UUID id,@PathVariable @DefaultValue("") String reason) {
        studentsInfoService.moveToArchiveById(id, reason);
        return new ResponseEntity<>("Contact moved to archive", HttpStatus.OK);
    }

    @PutMapping("/graduate/{id}")
    public ResponseEntity<String> graduateById(@PathVariable UUID id) {
        studentsInfoService.graduateById(id);
        return new ResponseEntity<>("Contact moved to archive", HttpStatus.OK);
    }


}
