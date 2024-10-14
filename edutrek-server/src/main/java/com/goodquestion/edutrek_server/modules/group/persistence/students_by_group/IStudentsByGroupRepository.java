package com.goodquestion.edutrek_server.modules.group.persistence.students_by_group;

import com.goodquestion.edutrek_server.modules.group.key.ComposeStudentsKey;
import com.goodquestion.edutrek_server.modules.group.persistence.IJunctionTableRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IStudentsByGroupRepository<T extends BaseStudentsByGroup> extends IJunctionTableRepository, JpaRepository<T, ComposeStudentsKey> {
   boolean existsByGroupIdAndStudentId(UUID groupId, UUID studentId);
   void deleteByGroupId(UUID groupId);
   List<T> getByGroupId(@Param("id") UUID uuid);
   Optional<BaseStudentsByGroup> getByGroupIdAndStudentId(UUID groupId, UUID studentId);
}