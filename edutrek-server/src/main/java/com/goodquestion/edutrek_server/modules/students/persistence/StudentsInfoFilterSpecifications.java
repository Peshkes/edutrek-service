package com.goodquestion.edutrek_server.modules.students.persistence;



import com.goodquestion.edutrek_server.modules.students.persistence.current.StudentEntity;
import org.springframework.data.jpa.domain.Specification;

public class StudentsInfoFilterSpecifications {


    public static Specification<StudentEntity> hasName(String name) {
        return (r, q, cb) -> cb.like(r.get("contact_name"), "%" + name + "%");
    }

    public static Specification<StudentEntity> hasPhone(String phone) {
        return (r, q, cb) -> cb.like(r.get("phone"), "%" + phone + "%");
    }

    public static Specification<StudentEntity> hasEmail(String email) {
        return (r, q, cb) -> cb.like(r.get("email"), "%" + email + "%");   }


    public static Specification<StudentEntity> hasStatusId(int status_id) {
        return (r, q, cb) -> cb.equal(r.get("status_id"), status_id);
    }

    public static Specification<StudentEntity> hasGroup(int group_id) {
        return (r, q, cb) -> cb.equal(r.get("group_id"), group_id);//TODO change binding!!!!!
    }

}
