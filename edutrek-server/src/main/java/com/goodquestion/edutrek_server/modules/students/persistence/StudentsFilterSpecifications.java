package com.goodquestion.edutrek_server.modules.students.persistence;


import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor
public class StudentsFilterSpecifications <E extends AbstractStudent> {


    public  Specification<E> hasName(String name) {
        return (r, q, cb) -> cb.like(r.get("contactName"), "%" + name + "%");
    }

    public  Specification<E> hasPhone(String phone) {
        return (r, q, cb) -> cb.like(r.get("phone"), "%" + phone + "%");
    }

    public Specification<E> hasEmail(String email) {
        return (r, q, cb) -> cb.like(r.get("email"), "%" + email + "%");   }


    public  Specification<E> hasStatusId(int status_id) {
        return (r, q, cb) -> cb.equal(r.get("statusId"), status_id);
    }

    public  Specification<E> hasGroup(int group_id) {
        return (r, q, cb) -> cb.equal(r.get("groupId"), group_id);//TODO change binding!!!!!
    }

    public Specification<E> getSpecifications( String search, Integer statusId, Integer groupId) {
        Specification<E> specs = Specification.where(null);
        if (statusId != null)
            specs = specs.and(this.hasStatusId(statusId));
        if (groupId != null)
            specs = specs.and(this.hasGroup(groupId));
        if (search != null && !search.isEmpty() && !search.isBlank()) {
            specs = specs.and(this.hasName(search))
                    .or(this.hasPhone(search))
                    .or(this.hasEmail(search));
        }
        return specs;
    }


}
