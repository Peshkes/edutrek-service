package com.goodquestion.edutrek_server.modules.studentInformation.persistence;



import org.springframework.data.jpa.domain.Specification;

public class StudentsInfoFilterSpecifications {


    public static Specification<StudentInfoEntity> hasName(String name) {
        return (r, q, cb) -> cb.like(r.get("contact_name"), "%" + name + "%");
    }

    public static Specification<StudentInfoEntity> hasPhone(String phone) {
        return (r, q, cb) -> cb.like(r.get("phone"), "%" + phone + "%");
    }

    public static Specification<StudentInfoEntity> hasEmail(String email) {
        return (r, q, cb) -> cb.like(r.get("email"), "%" + email + "%");   }


    public static Specification<StudentInfoEntity> hasStatusId(int status_id) {
        return (r, q, cb) -> cb.equal(r.get("status_id"), status_id);
    }

    public static Specification<StudentInfoEntity> hasGroup(int group_id) {
        return (r, q, cb) -> cb.equal(r.get("group_id"), group_id);//TODO change binding!!!!!
    }

}
