package com.goodquestion.edutrek_server.modules.studentInformation.service;
import com.goodquestion.edutrek_server.error.DatabaseException.*;
import com.goodquestion.edutrek_server.error.ShareException.*;
import com.goodquestion.edutrek_server.modules.contacts.persistence.ContactsRepository;
import com.goodquestion.edutrek_server.modules.studentInformation.archive.StudentInfoArchiveDocument;
import com.goodquestion.edutrek_server.modules.contacts.service.ContactsService;
import com.goodquestion.edutrek_server.modules.studentInformation.archive.StudentsInfoArchiveRepository;
import com.goodquestion.edutrek_server.modules.studentInformation.dto.*;
import com.goodquestion.edutrek_server.modules.studentInformation.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.UUID;

import static com.goodquestion.edutrek_server.error.ShareException.ContactAlreadyExistsException;
import static com.goodquestion.edutrek_server.error.ShareException.ContactNotFoundException;

@Service
@RequiredArgsConstructor
public class StudentsInfoService {

    private final StudentsInfoRepository repository;
    private final ContactsRepository contactRepository;
    private final ContactsService contactService;
    private final StudentsInfoArchiveRepository archiveRepository;


    public StudentInfoSearchDto getAll(int page, int pageSize, String search, Integer statusId, Integer groupId) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Specification<StudentInfoEntity> specs = Specification.where(null);
        if (statusId != null)
            specs = specs.and(StudentsInfoFilterSpecifications.hasStatusId(statusId));
        if (groupId != null)
            specs = specs.and(StudentsInfoFilterSpecifications.hasGroup(groupId));
        if (search != null && !search.isEmpty() && !search.isBlank()) {
            specs = specs.and(StudentsInfoFilterSpecifications.hasName(search))
                    .or(StudentsInfoFilterSpecifications.hasPhone(search))
                    .or(StudentsInfoFilterSpecifications.hasEmail(search));
        }
        Page<StudentInfoEntity> pageEntity = repository.findAll(specs, pageable);
        return new StudentInfoSearchDto(pageEntity.getContent(), page, pageSize, pageEntity.getTotalElements());
    }

    public StudentInfoEntity getById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ContactNotFoundException(id.toString()));
    }

    @Transactional
    public void addEntity(StudentsInfoDataDto contactData) {
        UUID contactId = contactService.addEntity(contactData);
        StudentInfoEntity entity = repository.findByPhoneOrEmail(contactData.getPhone(), contactData.getEmail());
        if (entity == null) {
            try {
                repository.save(new StudentInfoEntity(contactId, contactData.getFullPayment(), contactData.isDocumentsDone()));
            } catch (Exception e) {
                throw new DatabaseAddingException(e.getMessage());
            }
        } else
            throw new ContactAlreadyExistsException(contactData.getPhone(), contactData.getEmail());

    }

    @Transactional
    public void deleteById(UUID id) {
        if (!repository.existsById(id)) throw new StudentNotFoundException(id.toString());
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
        contactRepository.deleteById(id);
    }

    @Transactional
    public void updateById(UUID id, StudentsInfoDataDto contactData) {
        StudentInfoEntity entity = repository.findById(id).orElseThrow(() -> new StudentNotFoundException(id.toString()));
        contactService.updateById(id, contactData);
        entity.setFullPayment(contactData.getFullPayment());
        entity.setDocumentsDone(contactData.isDocumentsDone());
        try {
            repository.save(entity);
        } catch (Exception e) {
            throw new DatabaseUpdatingException(e.getMessage());
        }
    }

    @Transactional
    public void moveToArchiveById(UUID id, String reason) {
        StudentInfoEntity student = repository.findById(id).orElseThrow(() -> new StudentNotFoundException(id.toString()));
        try {
            archiveRepository.save(new StudentInfoArchiveDocument(id, student.getFullPayment(), student.isDocumentsDone(), LocalDate.now(),reason));
        } catch (Exception e) {
            throw new DatabaseAddingException(e.getMessage());
        }
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
        contactService.moveContactToArchiveById(id, reason);
    }

    @Transactional
    public void graduateById(UUID id) {
        moveToArchiveById(id,"Finished course and graduated");
    }
}