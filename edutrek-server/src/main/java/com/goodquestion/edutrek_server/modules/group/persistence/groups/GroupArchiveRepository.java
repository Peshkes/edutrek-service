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

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupArchiveRepository extends IGroupRepository<GroupArchiveEntity>, JpaRepository<GroupArchiveEntity, UUID>, JpaSpecificationExecutor<GroupArchiveEntity> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM ONLY groups_archive WHERE group_id = :id", nativeQuery = true)
    void deleteGroupByGroupId(UUID id);

    @Query(value = "SELECT * FROM ONLY groups_archive WHERE group_id = :id", nativeQuery = true)
    Optional<GroupArchiveEntity> getGroupByGroupId(@Param("id") UUID id);

    @Query("SELECT g FROM GroupArchiveEntity g")
    Page<GroupArchiveEntity> findAll(Specification<GroupArchiveEntity> spec, Pageable pageable);

    @Query(value = "SELECT CASE WHEN count(*) > 0 THEN true ELSE false END FROM ONLY groups_archive WHERE group_id = :id", nativeQuery = true)
    boolean checkGroupExistsById(@Param("id") UUID id);
}
