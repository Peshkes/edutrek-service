package com.goodquestion.edutrek_server.modules.contacts.persistence;

import com.goodquestion.edutrek_server.modules.branch.persistence.BranchRepository;
import org.springframework.data.jpa.domain.Specification;

public class ContactsFilterSpecifications {


    public static Specification<ContactsEntity> hasName(String name) {
        return (r, q, cb) -> cb.like(r.get("contact_name"), "%" + name + "%");
    }

    public static Specification<ContactsEntity> hasPhone(String phone) {
        return (r, q, cb) -> cb.like(r.get("phone"), "%" + phone + "%");
    }

    public static Specification<ContactsEntity> hasEmail(String email) {
        return (r, q, cb) -> cb.like(r.get("email"), "%" + email + "%");   }


    public static Specification<ContactsEntity> hasStatusId(int status_id) {
        return (r, q, cb) -> cb.equal(r.get("status_id"), status_id);
    }

}
