package com.goodquestion.edutrek_server.modules.students.persistence.archive;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentsArchiveRepository extends JpaRepository<StudentsArchiveEntity, UUID>, JpaSpecificationExecutor<StudentsArchiveEntity> {

}
