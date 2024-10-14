package com.goodquestion.edutrek_server.modules.group.persistence.groups;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupRepository extends IGroupRepository<GroupEntity>{
    List<GroupEntity> findActiveGroupsToGraduate(@Param("finishDate") LocalDate finishDate);
    boolean checkGroupExistsById(@Param("id") UUID id);
    Optional<BaseGroup> getGroupByGroupId(@Param("id") UUID id);
    Page<GroupEntity> findAll(Specification<GroupEntity> spec, Pageable pageable);
    @Modifying
    void deleteGroupByGroupId(@Param("id") UUID id);
}