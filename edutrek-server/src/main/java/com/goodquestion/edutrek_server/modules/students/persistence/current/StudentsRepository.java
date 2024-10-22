package com.goodquestion.edutrek_server.modules.students.persistence.current;


import com.goodquestion.edutrek_server.modules.students.persistence.IStudentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentsRepository extends IStudentRepository <StudentEntity>,JpaRepository<StudentEntity, UUID> , JpaSpecificationExecutor<StudentEntity>{

    StudentEntity findByPhoneOrEmail(String phone, String email);
}
