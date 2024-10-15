package com.goodquestion.edutrek_server.modules.contacts.service;

import com.goodquestion.edutrek_server.error.DatabaseException.*;

import static com.goodquestion.edutrek_server.error.ShareException.*;

import com.goodquestion.edutrek_server.modules.contacts.dto.*;
import com.goodquestion.edutrek_server.modules.contacts.persistence.*;
import com.goodquestion.edutrek_server.modules.contacts.persistence.archive.ContactArchiveEntity;
import com.goodquestion.edutrek_server.modules.contacts.persistence.archive.ContactsArchiveRepository;
import com.goodquestion.edutrek_server.modules.contacts.persistence.current.ContactsEntity;
import com.goodquestion.edutrek_server.modules.contacts.persistence.current.ContactsRepository;
import com.goodquestion.edutrek_server.modules.statuses.persistence.StatusRepository;
import com.goodquestion.edutrek_server.modules.students.dto.StudentsDataDto;
import com.goodquestion.edutrek_server.modules.students.persistence.current.StudentEntity;
import com.goodquestion.edutrek_server.modules.students.persistence.current.StudentsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactsService {

    private final ContactsArchiveRepository archiveRepository;
    private final ContactsRepository contactRepository;
    private final StudentsRepository studentRepository;
    private final StatusRepository statusRepository;
    private final RestTemplate restTemplate;

    @SuppressWarnings("unchecked")
    public ContactSearchDto getAll(int page, int pageSize, String search, Integer statusId) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Specification<ContactsEntity> contactSpecs = new ContactsFilterSpecifications<ContactsEntity>().getSpecifications(search,statusId);
        Specification<ContactArchiveEntity> contactArchiveSpecs = new ContactsFilterSpecifications<ContactArchiveEntity>().getSpecifications(search,statusId);
        Page<? extends AbstractContacts> pageCurrentEntity = contactRepository.findAll(contactSpecs, pageable);
        Page<? extends AbstractContacts> pageArchiveEntity = archiveRepository.findAll(contactArchiveSpecs,pageable);
        List<AbstractContacts> foundCurrent = (List<AbstractContacts>) pageCurrentEntity.getContent();
        List<AbstractContacts> foundArchive = (List<AbstractContacts>) pageArchiveEntity.getContent();
        foundCurrent.addAll(foundArchive);
        return new ContactSearchDto(foundCurrent, page, pageSize, (pageCurrentEntity.getTotalElements() + pageArchiveEntity.getTotalElements()));
    }


    public ContactsEntity getById(UUID id) {
        return contactRepository.findById(id).orElseThrow(() -> new ContactNotFoundException(id.toString()));
    }

    @Transactional

    public UUID addEntity(ContactsDataDto contactData) {
        ContactsEntity entity = contactRepository.findByPhoneOrEmail(contactData.getPhone(), contactData.getEmail());
        if (entity == null) {
            try {
                entity = new ContactsEntity(contactData.getContactName(), contactData.getPhone(), contactData.getEmail(), contactData.getStatusId(), contactData.getBranchId(), contactData.getTargetCourseId(), contactData.getComment());
                contactRepository.save(entity);
                return entity.getContactId();
            } catch (Exception e) {
                throw new DatabaseAddingException(e.getMessage());
            }
        } else
            throw new ContactAlreadyExistsException(contactData.getPhone(), contactData.getEmail());
    }

    @Transactional
    public void deleteById(UUID id) {
        if (studentRepository.existsById(id)) {
            restTemplate.delete("http://localhost:8080/students/" + id);
        } else {
            deleteContactById(id);
        }
    }

    @Transactional
    public void deleteContactById(UUID id) {
        if (!contactRepository.existsById(id)) throw new ContactNotFoundException(id.toString());
        try {
            contactRepository.deleteById(id);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
    }


    @Transactional
    public void updateById(UUID id, ContactsDataDto contactData) {
        ContactsEntity entity = contactRepository.findById(id).orElseThrow(() -> new ContactNotFoundException(String.valueOf(id.toString())));
        entity.setContactName(contactData.getContactName());
        entity.setPhone(contactData.getPhone());
        entity.setEmail(contactData.getEmail());
        entity.setComment(contactData.getComment());
        entity.setStatusId(contactData.getStatusId());
        entity.setBranchId(contactData.getBranchId());
        entity.setTargetCourseId(contactData.getTargetCourseId());
        try {
            contactRepository.save(entity);
        } catch (Exception e) {
            throw new DatabaseUpdatingException(e.getMessage());
        }
    }

    @Transactional
    public void moveToArchiveById(UUID id, String reason) {
        if (studentRepository.existsById(id)) {
            restTemplate.put("http://localhost:8080/students/archive/" + id + "/" + reason, null);
        } else {
            moveContactToArchiveById(id, reason);
        }
    }

    @Transactional
    public void moveContactToArchiveById(UUID id, String reason) {
        ContactsEntity contact = contactRepository.findById(id).orElseThrow(() -> new ContactNotFoundException(id.toString()));
        int statusId = statusRepository.getStatusEntityByStatusName("Archive").getStatusId();
        contact.setStatusId(statusId);
        ContactArchiveEntity contactArchEntity = new ContactArchiveEntity(contact,reason);
        try {
            contactRepository.deleteContactById(id);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
        try {
            archiveRepository.save(contactArchEntity);
        } catch (Exception e) {
            throw new DatabaseAddingException(e.getMessage());
        }
    }

    @Transactional
    public void promoteContactToStudentById(UUID id, StudentsDataDto studentData) {
        ContactsEntity contact = contactRepository.findById(id).orElseThrow(() -> new ContactNotFoundException(id.toString()));
        if (!studentRepository.existsById(id)) {
            int statusId = statusRepository.getStatusEntityByStatusName("Student").getStatusId();
            contact.setStatusId(statusId);
            try {
                studentRepository.save(new StudentEntity(studentData));
            } catch (Exception e) {
                throw new DatabaseAddingException(e.getMessage());
            }
            try {
                contactRepository.deleteById(id);
            } catch (Exception e) {
                throw new DatabaseDeletingException(e.getMessage());
            }
        }
    }
}
