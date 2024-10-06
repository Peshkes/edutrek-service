package com.goodquestion.edutrek_server.modules.group.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, UUID>, JpaSpecificationExecutor<GroupEntity> {
    @Query("SELECT g FROM GroupEntity g WHERE g.isActive = true AND g.finishDate <= :finishDate AND g.deactivateAfter = true")
    List<GroupEntity> findActiveGroupsToGraduate(@Param("finishDate") LocalDate finishDate);
}