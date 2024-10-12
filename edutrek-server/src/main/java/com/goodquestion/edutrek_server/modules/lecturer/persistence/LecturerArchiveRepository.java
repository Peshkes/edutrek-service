package com.goodquestion.edutrek_server.modules.lecturer.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LecturerArchiveRepository extends ILecturerRepository, JpaRepository<LecturerArchiveEntity, UUID>, JpaSpecificationExecutor<LecturerArchiveEntity> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM ONLY lecturers_archive WHERE lecturer_id = :id", nativeQuery = true)
    int deleteLecturerById(UUID id);

    @Query(value = "SELECT * FROM ONLY lecturers_archive WHERE lecturer_id = :id", nativeQuery = true)
    Optional<LecturerArchiveEntity> getLecturerById(UUID id);
}
