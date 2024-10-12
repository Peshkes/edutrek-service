package com.goodquestion.edutrek_server.modules.group.persistence.lecturers_by_group;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LecturersByGroupRepository extends ILecturerByGroupRepository<LecturersByGroupEntity>{
    @Query(value = "DELETE FROM ONLY lecturers_by_group WHERE group_id = :id", nativeQuery = true)
    void deleteByGroupId(@Param("id") UUID groupId);
    @Query(value = "SELECT * FROM ONLY lecturers_by_group WHERE group_id = :id", nativeQuery = true)
    List<LecturersByGroupEntity> getByGroupId(@Param("id") UUID uuid);
}
