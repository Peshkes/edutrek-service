package com.goodquestion.edutrek_server.modules.lecturer.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface ILecturerRepository<T extends BaseLecturer> extends JpaRepository<T, UUID>, JpaSpecificationExecutor<T> {
    Optional<BaseLecturer> getLecturerByLecturerId(UUID id);
}
