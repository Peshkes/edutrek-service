package com.goodquestion.edutrek_server.utility_service;

import com.goodquestion.edutrek_server.modules.contacts.persistence.AbstractContacts;
import com.goodquestion.edutrek_server.modules.contacts.persistence.ContactsFilterSpecifications;
import com.goodquestion.edutrek_server.modules.contacts.persistence.IContactRepository;
import com.goodquestion.edutrek_server.modules.students.dto.FoundEntitiesDto;
import com.goodquestion.edutrek_server.modules.students.persistence.AbstractStudent;
import com.goodquestion.edutrek_server.modules.students.persistence.IStudentRepository;
import com.goodquestion.edutrek_server.modules.students.persistence.StudentsFilterSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;


import java.util.List;
import java.util.UUID;

public class SearchUtilityMethods {
    @SuppressWarnings("unchecked")
    public static <C extends AbstractContacts, E extends IContactRepository<C>> FoundEntitiesDto findContacts(Pageable pageable, String search, Integer statusId, UUID courseId, E repository) {
        Specification<C> contactSpecs = new ContactsFilterSpecifications<C>().getSpecifications(search, statusId, courseId);
        Page<? extends AbstractContacts> pageContactEntity = repository.findAll(contactSpecs, pageable);
        return new FoundEntitiesDto((List<AbstractContacts>) pageContactEntity.getContent(), null, pageContactEntity.getTotalElements());
    }

    @SuppressWarnings("unchecked")
    public static <S extends AbstractStudent, SR extends IStudentRepository<S>> FoundEntitiesDto findStudents(Pageable pageable, String search, Integer statusId, Integer group_id, SR repository) {
        Specification<S> studentSpecs = new StudentsFilterSpecifications<S>().getSpecifications(search, statusId, group_id);
        Page<? extends AbstractStudent> pageContactEntity = repository.findAll(studentSpecs, pageable);
        return new FoundEntitiesDto(null, (List<AbstractStudent>) pageContactEntity.getContent(), pageContactEntity.getTotalElements());
    }


    public static <C extends AbstractContacts, CR extends IContactRepository<C>, S extends AbstractStudent, SR extends IStudentRepository<S>> FoundEntitiesDto findContactsAndStudents(Pageable pageable, String search, Integer statusId, UUID courseId, CR contactsRepository, SR studentsRepository, int page, int pageSize) {
        FoundEntitiesDto foundContactEntities = findContacts(pageable, search, statusId, courseId, contactsRepository);
        List<AbstractContacts> foundContact = foundContactEntities.getFoundContacts();
        if (foundContact.size() < pageSize) {
            addStudents(search, statusId, studentsRepository, page, pageSize, foundContact);
        }
        return new FoundEntitiesDto(foundContact, null, foundContactEntities.getTotalElements());
    }

    public static <S extends AbstractStudent, SR extends IStudentRepository<S>> void addStudents(String search, Integer statusId, SR repository, int page, int pageSize, List<AbstractContacts> foundContact) {
        FoundEntitiesDto foundStudentEntities = findStudents(PageRequest.of(page, pageSize - foundContact.size()), search, statusId, null, repository);
        List<AbstractStudent> foundStudents = foundStudentEntities.getFoundStudents();
        if (!foundStudents.isEmpty()) {
            List<? extends AbstractContacts> contactFromStudents = foundStudents.stream().map(AbstractContacts::new).toList();
            foundContact.addAll(contactFromStudents);
        }
    }
}
