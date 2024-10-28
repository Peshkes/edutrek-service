package com.goodquestion.edutrek_server.modules.contacts.controller;


import com.goodquestion.edutrek_server.modules.contacts.dto.ContactSearchDto;
import com.goodquestion.edutrek_server.modules.contacts.dto.ContactsDataDto;
import com.goodquestion.edutrek_server.modules.contacts.persistence.AbstractContacts;
import com.goodquestion.edutrek_server.modules.contacts.service.ContactsService;
import com.goodquestion.edutrek_server.modules.students.dto.StudentsFromContactDataDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactsController {

    private final ContactsService contactsService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ContactSearchDto getAll(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "pagesize",defaultValue = "10") int pageSize,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "statusid", required = false) Integer statusId,
            @RequestParam(name = "targetcourseid", required = false) UUID courseId
    ) {
        return contactsService.getAll(page, pageSize, search, statusId, courseId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AbstractContacts getById(@PathVariable UUID id) {
        return contactsService.getById(id);
    }

    @PostMapping("")
    public ResponseEntity<String> addEntity(@RequestBody @Valid ContactsDataDto contactData) {
        contactsService.addEntity(contactData);
        return new ResponseEntity<>("Contact created", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable UUID id) {
        contactsService.deleteById(id);
        return new ResponseEntity<>("Contact deleted", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateById(@PathVariable UUID id, @RequestBody @Valid ContactsDataDto contactData) {
        contactsService.updateById(id, contactData);
        return new ResponseEntity<>("Contact updated", HttpStatus.OK);
    }

    @PutMapping("/archive/{id}/{reason}")
    public ResponseEntity<String> moveToArchiveById(@PathVariable UUID id,@PathVariable @DefaultValue("") String reason) {
        contactsService.moveToArchiveById(id,reason);
        return new ResponseEntity<>("Contact moved to archive", HttpStatus.OK);
    }

    @PostMapping("/promote/{id}")
    public ResponseEntity<String> promoteContactToStudentById(@PathVariable UUID id,@RequestBody @Valid StudentsFromContactDataDto studentData) {
        contactsService.promoteContactToStudentById(id,studentData);
        return new ResponseEntity<>("Contact promoted to student", HttpStatus.OK);
    }






}
