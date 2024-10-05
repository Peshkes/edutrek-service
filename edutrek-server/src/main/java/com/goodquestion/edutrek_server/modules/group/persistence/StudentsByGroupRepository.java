package com.goodquestion.edutrek_server.modules.group.persistence;

import com.goodquestion.edutrek_server.modules.group.key.ComposeStudentsKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudentsByGroupRepository extends JpaRepository<StudentsByGroupEntity, ComposeStudentsKey> {
   boolean existsByGroupIdAndStudentId(UUID groupId, UUID studentId);
}
