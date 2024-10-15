package com.goodquestion.edutrek_server.modules.contacts.persistence;



import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
@NoArgsConstructor
public class  ContactsFilterSpecifications <E extends AbstractContacts>{


    public Specification<E> hasName(String name) {
        return (r, q, cb) -> cb.like(r.get("contact_name"), "%" + name + "%");
    }

    public Specification<E> hasPhone(String phone) {
        return (r, q, cb) -> cb.like(r.get("phone"), "%" + phone + "%");
    }

    public Specification<E> hasEmail(String email) {
        return (r, q, cb) -> cb.like(r.get("email"), "%" + email + "%");   }


    public Specification<E> hasStatusId(int status_id) {
        return (r, q, cb) -> cb.equal(r.get("status_id"), status_id);
    }


    public Specification<E> getSpecifications( String search, Integer statusId) {
        Specification<E> specs = Specification.where(null);
        if (statusId != null)
            specs = specs.and(this.hasStatusId(statusId));
        if (search != null && !search.isEmpty() && !search.isBlank()) {
            specs = specs.and(this.hasName(search))
                    .or(this.hasPhone(search))
                    .or(this.hasEmail(search));
        }
        return specs;
    }


}
