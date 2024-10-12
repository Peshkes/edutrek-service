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
public interface LecturerRepository extends ILecturerRepository, JpaRepository<LecturerEntity, UUID>, JpaSpecificationExecutor<LecturerEntity> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM ONLY lecturers WHERE lecturer_id = :id", nativeQuery = true)
    int deleteLecturerById(UUID id);

    @Query(value = "SELECT * FROM ONLY lecturers WHERE lecturer_id = :id", nativeQuery = true)
    Optional<LecturerEntity> getLecturerById(UUID id);
}
