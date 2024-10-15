package com.goodquestion.edutrek_server.modules.students.service;

import com.goodquestion.edutrek_server.error.DatabaseException.*;
import com.goodquestion.edutrek_server.error.ShareException.*;
import com.goodquestion.edutrek_server.modules.contacts.dto.ContactsDataDto;
import com.goodquestion.edutrek_server.modules.contacts.persistence.current.ContactsEntity;
import com.goodquestion.edutrek_server.modules.contacts.persistence.current.ContactsRepository;

import com.goodquestion.edutrek_server.modules.contacts.service.ContactsService;
import com.goodquestion.edutrek_server.modules.students.persistence.archive.StudentsArchiveEntity;
import com.goodquestion.edutrek_server.modules.students.persistence.archive.StudentsArchiveRepository;
import com.goodquestion.edutrek_server.modules.students.dto.*;
import com.goodquestion.edutrek_server.modules.students.persistence.*;
import com.goodquestion.edutrek_server.modules.students.persistence.current.StudentEntity;
import com.goodquestion.edutrek_server.modules.students.persistence.current.StudentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.UUID;

import static com.goodquestion.edutrek_server.error.ShareException.ContactNotFoundException;

@Service
@RequiredArgsConstructor

public class StudentsService {

    private final StudentsRepository repository;
    private final ContactsRepository contactsRepository;
    private final ContactsService contactService;
    private final StudentsArchiveRepository archiveRepository;


    public StudentSearchDto getAll(int page, int pageSize, String search, Integer statusId, Integer groupId) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Specification<StudentEntity> specs = Specification.where(null);
        if (statusId != null)
            specs = specs.and(StudentsInfoFilterSpecifications.hasStatusId(statusId));
        if (groupId != null)
            specs = specs.and(StudentsInfoFilterSpecifications.hasGroup(groupId));
        if (search != null && !search.isEmpty() && !search.isBlank()) {
            specs = specs.and(StudentsInfoFilterSpecifications.hasName(search))
                    .or(StudentsInfoFilterSpecifications.hasPhone(search))
                    .or(StudentsInfoFilterSpecifications.hasEmail(search));
        }
        Page<StudentEntity> pageEntity = repository.findAll(specs, pageable);
        return new StudentSearchDto(pageEntity.getContent(), page, pageSize, pageEntity.getTotalElements());
    }

    public StudentEntity getById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ContactNotFoundException(id.toString()));
    }

    @Transactional
    public void addEntity(ContactsDataDto contactData, StudentsDataDto studentData) {
        ContactsEntity contact = contactsRepository.findByPhoneOrEmail(contactData.getPhone(), contactData.getEmail());
        if (contact == null) {
//            UUID contactId = contactService.addEntity(contactData);
//            contactService.promoteContactToStudentById(contactId, studentData);
            repository.save(new StudentEntity(
                    studentData));
        } else {
            contactService.promoteContactToStudentById(contact.getContactId(), studentData);
        }
    }


    @Transactional
    public void deleteById(UUID id) {
        if (!repository.existsById(id)) throw new StudentNotFoundException(id.toString());
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
        if (contactsRepository.existsById(id))
            contactsRepository.deleteById(id);
    }

    @Transactional
    public void updateById(UUID id, StudentsInfoDataFromContactDto studentData) {
        StudentEntity entity = repository.findById(id).orElseThrow(() -> new StudentNotFoundException(id.toString()));
        contactService.updateById(id, studentData);
        entity.setFullPayment(studentData.getFullPayment());
        entity.setDocumentsDone(studentData.isDocumentsDone());
        try {
            repository.save(entity);
        } catch (Exception e) {
            throw new DatabaseUpdatingException(e.getMessage());
        }
    }

    @Transactional
    public void moveToArchiveById(UUID id, String reason) {
        StudentEntity student = repository.findById(id).orElseThrow(() -> new StudentNotFoundException(id.toString()));
        try {
            archiveRepository.save(new StudentsArchiveEntity(student, reason));
        } catch (Exception e) {
            throw new DatabaseAddingException(e.getMessage());
        }
        //contactService.moveContactToArchiveById(id, reason);
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
    }

    @Transactional
    public void graduateById(UUID id) {
        moveToArchiveById(id, "Finished course and graduated");
    }
}
