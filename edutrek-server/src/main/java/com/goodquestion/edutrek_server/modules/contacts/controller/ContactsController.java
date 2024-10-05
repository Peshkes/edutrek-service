package com.goodquestion.edutrek_server.modules.contacts.controller;



import com.goodquestion.edutrek_server.modules.contacts.dto.ContactsDataDto;
import com.goodquestion.edutrek_server.modules.contacts.persistence.ContactsEntity;
import com.goodquestion.edutrek_server.modules.contacts.service.ContactsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactsController {

    private final ContactsService contactsService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Page<ContactsEntity> getAll(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int statusId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return contactsService.getAll(search, statusId, page, pageSize);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ContactsEntity getById(@PathVariable UUID id) {
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

    @PutMapping("/graduate/{id}")
    public ResponseEntity<String> graduateById(@PathVariable UUID id) {
        contactsService.graduateById(id);
        return new ResponseEntity<>("Contact moved to archive", HttpStatus.OK);
    }


}
