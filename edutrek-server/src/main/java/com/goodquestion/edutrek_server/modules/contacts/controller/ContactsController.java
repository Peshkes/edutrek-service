package com.goodquestion.edutrek_server.modules.contacts.controller;


import com.goodquestion.edutrek_server.modules.contacts.dto.ContactSearchDto;
import com.goodquestion.edutrek_server.modules.contacts.dto.ContactsDataDto;
import com.goodquestion.edutrek_server.modules.contacts.persistence.ContactsEntity;
import com.goodquestion.edutrek_server.modules.contacts.service.ContactsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


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
    public ContactSearchDto getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer statusId
    ) {
        return contactsService.getAll(page, pageSize, search, statusId);
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
