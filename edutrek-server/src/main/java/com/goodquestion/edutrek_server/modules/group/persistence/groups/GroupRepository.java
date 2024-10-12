package com.goodquestion.edutrek_server.modules.group.persistence.groups;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupRepository extends IGroupRepository<GroupEntity>, JpaRepository<GroupEntity, UUID>, JpaSpecificationExecutor<GroupEntity> {
//    @Query(value = "SELECT * FROM ONLY groups WHERE is_active = true AND finish_date <= :finishDate AND deactivate_after = true", nativeQuery = true)
//    List<GroupEntity> findActiveGroupsToGraduate(@Param("finishDate") LocalDate finishDate);

    @Query(value = "SELECT CASE WHEN count(*) > 0 THEN true ELSE false END FROM ONLY groups WHERE group_id = :id", nativeQuery = true)
    boolean checkGroupExistsById(@Param("id") UUID id);

    @Query(value = "SELECT * FROM ONLY groups WHERE group_id = :id", nativeQuery = true)
    Optional<GroupEntity> getGroupByGroupId(@Param("id") UUID id);

    @Query("SELECT g FROM GroupEntity g")
    Page<GroupEntity> findAll(Specification<GroupEntity> spec, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM ONLY groups WHERE group_id = :id", nativeQuery = true)
    void deleteGroupByGroupId(@Param("id") UUID id);
}