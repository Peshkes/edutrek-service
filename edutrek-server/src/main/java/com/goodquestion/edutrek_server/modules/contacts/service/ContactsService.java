package com.goodquestion.edutrek_server.modules.contacts.service;

import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseAddingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseDeletingException;
import com.goodquestion.edutrek_server.modules.contacts.dto.ContactSearchDto;
import com.goodquestion.edutrek_server.modules.contacts.dto.ContactsDataDto;
import com.goodquestion.edutrek_server.modules.contacts.persistence.AbstractContacts;
import com.goodquestion.edutrek_server.modules.contacts.persistence.archive.ContactArchiveEntity;
import com.goodquestion.edutrek_server.modules.contacts.persistence.archive.ContactsArchiveRepository;
import com.goodquestion.edutrek_server.modules.contacts.persistence.current.ContactsEntity;
import com.goodquestion.edutrek_server.modules.contacts.persistence.current.ContactsRepository;
import com.goodquestion.edutrek_server.modules.statuses.persistence.StatusEntity;
import com.goodquestion.edutrek_server.modules.statuses.persistence.StatusRepository;
import com.goodquestion.edutrek_server.modules.students.dto.FoundEntitiesDto;
import com.goodquestion.edutrek_server.modules.students.dto.StudentsDataDto;
import com.goodquestion.edutrek_server.modules.students.persistence.AbstractStudent;


import com.goodquestion.edutrek_server.modules.students.persistence.archive.StudentsArchiveRepository;
import com.goodquestion.edutrek_server.modules.students.persistence.current.StudentEntity;
import com.goodquestion.edutrek_server.modules.students.persistence.current.StudentsRepository;
import static com.goodquestion.edutrek_server.utility_service.SearchUtilityMethods.*;
import com.goodquestion.edutrek_server.utility_service.logging.Loggable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.goodquestion.edutrek_server.error.ShareException.ContactAlreadyExistsException;
import static com.goodquestion.edutrek_server.error.ShareException.ContactNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactsService {

    private final ContactsArchiveRepository contactArchiveRepository;
    private final StudentsArchiveRepository studentArchiveRepository;
    private final ContactsRepository contactRepository;
    private final StudentsRepository studentRepository;
    private final StatusRepository statusRepository;


    @Loggable
    public ContactSearchDto getAll(int page, int pageSize, String search, Integer statusId, UUID courseId) {
        StatusEntity status = statusId == null ? null : statusRepository.findById(statusId).orElse(null);
        Pageable pageable = PageRequest.of(page, pageSize);
        if (status != null && status.getStatusName().equalsIgnoreCase("Archive")) {
            FoundEntitiesDto foundContactArchive = findContactsAndStudents(pageable, search, statusId, courseId, contactArchiveRepository, studentArchiveRepository, page, pageSize);
            return new ContactSearchDto(foundContactArchive.getFoundContacts(), page, pageSize, foundContactArchive.getTotalElements());
        } else if (status != null && status.getStatusName().equalsIgnoreCase("Student")) {            
            FoundEntitiesDto foundStudentEntities = findStudents(pageable, search, statusId, null, studentRepository);
            List<AbstractStudent> foundStudents = foundStudentEntities.getFoundStudents();
            if (foundStudents.size() < pageSize) {
                FoundEntitiesDto foundStudentArchiveEntities = findStudents(PageRequest.of(page, pageSize - foundStudents.size()), search, statusId, null, studentArchiveRepository);
                List<AbstractStudent> foundStudentArchive = foundStudentArchiveEntities.getFoundStudents();
                if (!foundStudentArchive.isEmpty())
                    foundStudents.addAll(foundStudentArchive);
            }
            List<? extends AbstractContacts> contactFromStudents = foundStudents.stream().map(AbstractContacts::new).collect(Collectors.toList());
            return new ContactSearchDto(contactFromStudents, page, pageSize, foundStudentEntities.getTotalElements());
        } else {            
            FoundEntitiesDto foundContactDto = findContactsAndStudents(pageable, search, statusId, courseId, contactRepository, studentRepository, page, pageSize);
            List<AbstractContacts> foundContacts = foundContactDto.getFoundContacts();
            if (foundContacts.size() < pageSize) {
                FoundEntitiesDto foundContactArchiveEntities = findContacts(PageRequest.of(page, pageSize - foundContacts.size()), search, statusId, courseId, contactArchiveRepository);
                List<AbstractContacts> foundContactsArchive = foundContactArchiveEntities.getFoundContacts();
                if (!foundContactsArchive.isEmpty()) {
                    foundContacts.addAll(foundContactsArchive);
                }
            }
            if (foundContacts.size() < pageSize)
                addStudents(search, statusId, studentArchiveRepository, page, pageSize, foundContacts);
            return new ContactSearchDto(foundContacts, page, pageSize, (foundContactDto.getTotalElements()));
        }
    }

    @Loggable
    public AbstractContacts getById(UUID id) {
        return contactRepository.getByContactId(id).or(() -> contactArchiveRepository.findById(id)).orElseThrow(() -> new ContactNotFoundException(id.toString()));
    }

    @Loggable
    @Transactional
    public void addEntity(ContactsDataDto contactData) {
        if (!contactRepository.existsByPhoneOrEmail(contactData.getPhone(), contactData.getEmail())) {
            try {
                contactRepository.save(new ContactsEntity(contactData.getContactName(), contactData.getPhone(), contactData.getEmail(), contactData.getStatusId(), contactData.getBranchId(), contactData.getTargetCourseId(), contactData.getComment()));
            } catch (Exception e) {
                throw new DatabaseAddingException(e.getMessage());
            }
        } else
            throw new ContactAlreadyExistsException(contactData.getPhone(), contactData.getEmail());
    }

    @Loggable
    @Transactional
    public void deleteById(UUID id) {
        if (!contactRepository.existsById(id)) throw new ContactNotFoundException(id.toString());
        try {
            contactRepository.deleteById(id);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
    }

    @Loggable
    @Transactional
    public void updateById(UUID id, ContactsDataDto contactData) {
            AbstractContacts entity = contactRepository.getByContactId(id).or(() -> contactArchiveRepository.findById(id)).orElseThrow(() -> new ContactNotFoundException(id.toString()));
            updateEntity(contactData, entity);
    }

    private <T extends AbstractContacts> void updateEntity(ContactsDataDto contactData, T entity) {
        entity.setContactName(contactData.getContactName());
        entity.setPhone(contactData.getPhone());
        entity.setEmail(contactData.getEmail());
        entity.setComment(contactData.getComment());
        entity.setStatusId(contactData.getStatusId());
        entity.setBranchId(contactData.getBranchId());
        entity.setTargetCourseId(contactData.getTargetCourseId());
    }

    @Loggable
    @Transactional
    public void moveToArchiveById(UUID id, String reason) {
        ContactsEntity contact = contactRepository.findById(id).orElseThrow(() -> new ContactNotFoundException(id.toString()));
        int statusId = statusRepository.findStatusEntityByStatusName("Archive").getStatusId();
        contact.setStatusId(statusId);
        ContactArchiveEntity contactArchEntity = new ContactArchiveEntity(contact, reason);
        try {
            contactRepository.deleteContactById(id);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
        try {
            contactArchiveRepository.save(contactArchEntity);
        } catch (Exception e) {
            throw new DatabaseAddingException(e.getMessage());
        }
    }

    @Loggable
    @Transactional
    public void promoteContactToStudentById(UUID id, StudentsDataDto studentData) {
        ContactsEntity contact = contactRepository.findById(id).orElseThrow(() -> new ContactNotFoundException(id.toString()));
        if (!studentRepository.existsById(id)) {
            int statusId = statusRepository.findStatusEntityByStatusName("Student").getStatusId();
            contact.setStatusId(statusId);
            try {
                studentRepository.save(new StudentEntity(id, studentData));
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
