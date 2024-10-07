package com.goodquestion.edutrek_server.modules.contacts.service;

import com.goodquestion.edutrek_server.error.DatabaseException.*;

import static com.goodquestion.edutrek_server.error.ShareException.*;

import com.goodquestion.edutrek_server.modules.contacts.archive.*;
import com.goodquestion.edutrek_server.modules.contacts.dto.*;
import com.goodquestion.edutrek_server.modules.contacts.persistence.*;
import com.goodquestion.edutrek_server.modules.statuses.persistence.StatusRepository;
import com.goodquestion.edutrek_server.modules.studentInformation.persistence.StudentsInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContactsService {

    private final ContactsArchiveRepository archiveRepository;
    private final ContactsRepository contactRepository;
    private final StudentsInfoRepository studentRepository;
    private final StatusRepository statusRepository;

    private final RestTemplate restTemplate;


    public ContactSearchDto getAll(int page, int pageSize, String search, Integer statusId) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Specification<ContactsEntity> specs = Specification.where(null);
        if (statusId != null)
            specs = specs.and(ContactsFilterSpecifications.hasStatusId(statusId));
        if (search != null && !search.isEmpty() && !search.isBlank()) {
            specs = specs.and(ContactsFilterSpecifications.hasName(search))
                    .or(ContactsFilterSpecifications.hasPhone(search))
                    .or(ContactsFilterSpecifications.hasEmail(search));
        }
        Page<ContactsEntity> pageEntity = contactRepository.findAll(specs, pageable);
        return new ContactSearchDto(pageEntity.getContent(), page, pageSize, pageEntity.getTotalElements());
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
            restTemplate.delete("http://localhost:8080/students/{id}");
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
            restTemplate.put("http://localhost:8080/students/archive/{id}/{reason}",null);
        } else {
            moveContactToArchiveById(id, reason);
        }
    }

    @Transactional
    public void moveContactToArchiveById(UUID id, String reason) {
            ContactsEntity contact = contactRepository.findById(id).orElseThrow(() -> new ContactNotFoundException(id.toString()));

            int statusId = statusRepository.getStatusIdByStatusName("Archive");
            contact.setStatusId(statusId);
            try {
                archiveRepository.save(new ContactArchiveDocument(id, contact.getContactName(), contact.getPhone(), contact.getEmail(), contact.getStatusId(), contact.getBranchId(), contact.getTargetCourseId(), reason, LocalDate.now()));
            } catch (Exception e) {
                throw new DatabaseAddingException(e.getMessage());
            }
            try {
                contactRepository.deleteById(id);
            } catch (Exception e) {

                throw new DatabaseDeletingException("Couldn't move contact to archive. " + e.getMessage());
            }
    }

}
