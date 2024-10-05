package com.goodquestion.edutrek_server.modules.lecturer.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LecturerRepository extends JpaRepository<LecturerEntity, UUID> { }
