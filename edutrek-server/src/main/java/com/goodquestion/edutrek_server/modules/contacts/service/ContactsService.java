package com.goodquestion.edutrek_server.modules.contacts.service;

import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseAddingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseDeletingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseUpdatingException;

import static com.goodquestion.edutrek_server.error.ShareException.*;

import com.goodquestion.edutrek_server.modules.ContactsArchive.persistence.ContactArchiveEntity;
import com.goodquestion.edutrek_server.modules.ContactsArchive.persistence.ContactsArchiveRepository;
import com.goodquestion.edutrek_server.modules.contacts.dto.ContactsDataDto;
import com.goodquestion.edutrek_server.modules.contacts.persistence.ContactsEntity;
import com.goodquestion.edutrek_server.modules.contacts.persistence.ContactsRepository;
import com.goodquestion.edutrek_server.modules.statuses.persistence.StatusEntity;
import com.goodquestion.edutrek_server.modules.statuses.persistence.StatusRepository;
import com.goodquestion.edutrek_server.modules.statuses.service.StatusService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContactsService {

    private final ContactsRepository repository;
    private final ContactsArchiveRepository archiveRepository;
    private final StatusService statusService;
    private final StatusRepository statusRepository;

    public Page<ContactsEntity> getAll(String search, int statusId, int page, int pageSize) {
        String status;
        Pageable pageable;
        if (statusId > 0) {
            status = statusService.getById(statusId).getStatusName();
            pageable = PageRequest.of(page, pageSize, Sort.by(status));
        } else
            pageable = PageRequest.of(page, pageSize);
        if(!search.isBlank() && !search.isEmpty()){
            Specification<ContactsEntity> specs = new Specification<ContactsEntity>() {
                @Override
                public Predicate toPredicate(Root<ContactsEntity> r, CriteriaQuery<?> q, CriteriaBuilder cb) {
                    return cb.like(r.get("contact_name"), "%" + search + "%");
                }
            };
            return repository.findAll(specs,pageable);
        }
        return repository.findAll(pageable);
    }

    public ContactsEntity getById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ContactNotFoundException(id.toString()));
    }

    @Transactional
    public void addEntity(ContactsDataDto contactData) {
        ContactsEntity entity = repository.findByPhoneOrEmail(contactData.getPhone(), contactData.getEmail());
        if (entity == null) {
            try {
                repository.save(new ContactsEntity(contactData.getContactName(), contactData.getPhone(), contactData.getEmail(), contactData.getStatusId(), contactData.getBranchId(), contactData.getTargetCourseId(), contactData.getComment()));
            } catch (Exception e) {
                throw new DatabaseAddingException(e.getMessage());
            }
        } else
            throw new ContactAlreadyExistsException(contactData.getPhone(), contactData.getEmail());

    }

    @Transactional
    public void deleteById(UUID id) {
        if (!repository.existsById(id)) throw new ContactNotFoundException(id.toString());
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
    }

    @Transactional
    public void updateById(UUID id, ContactsDataDto contactData) {
        ContactsEntity branchEntity = repository.findById(id).orElseThrow(() -> new ContactNotFoundException(String.valueOf(id.toString())));
        branchEntity.setContactName(contactData.getContactName());
        branchEntity.setPhone(contactData.getPhone());
        branchEntity.setEmail(contactData.getEmail());
        branchEntity.setComment(contactData.getComment());
        try {
            repository.save(branchEntity);
        } catch (Exception e) {
            throw new DatabaseUpdatingException(e.getMessage());
        }
    }

    @Transactional
    public void graduateById(UUID id) {
        ContactsEntity contact = repository.findById(id).orElseThrow(() -> new ContactNotFoundException(id.toString()));
        List<Integer> archive = statusService.getAll().stream().filter(s -> s.getStatusName().equalsIgnoreCase("Archive")).map(StatusEntity::getStatusId).toList();
        contact.setStatusId(archive.getFirst());
        try {
            String reason = "Finished course and graduated";//TODO maybe ad as a variable
            archiveRepository.save(new ContactArchiveEntity(id, contact.getContactName(), contact.getPhone(), contact.getEmail(), contact.getStatusId(), contact.getBranchId(), contact.getTargetCourseId(), reason, LocalDate.now()));
            repository.deleteById(id);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
    }
}
