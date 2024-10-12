package com.goodquestion.edutrek_server.modules.group.persistence.students_by_group;

import com.goodquestion.edutrek_server.modules.group.key.ComposeStudentsKey;
import com.goodquestion.edutrek_server.modules.group.persistence.IJunctionTableRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IStudentsByGroupRepository<T extends StudentsByGroupEntity> extends IJunctionTableRepository, JpaRepository<T, ComposeStudentsKey> {
   boolean existsByGroupIdAndStudentId(UUID groupId, UUID studentId);
   void deleteByGroupId(UUID groupId);
   List<T> getByGroupId(UUID uuid);
   Optional<T> getByGroupIdAndStudentId(UUID groupId, UUID studentId);
}