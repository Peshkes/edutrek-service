package com.goodquestion.edutrek_server.modules.group.junction_persistence;

import com.goodquestion.edutrek_server.modules.group.key.ComposeLecturerKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LecturersByGroupRepository extends JpaRepository<LecturersByGroupEntity, ComposeLecturerKey> {
    void deleteByGroupId(UUID groupId);
}
