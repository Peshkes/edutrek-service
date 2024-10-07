package com.goodquestion.edutrek_server.modules.studentInformation.persistence;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentsInfoRepository extends JpaRepository<StudentInfoEntity, UUID> , JpaSpecificationExecutor<StudentInfoEntity>{

    StudentInfoEntity findByPhoneOrEmail(String phone, String email);
}
