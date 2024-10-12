package com.goodquestion.edutrek_server.modules.lecturer.persistence;

import java.util.Optional;
import java.util.UUID;

public interface ILecturerRepository {
    int deleteLecturerById(UUID id);
    Optional<? extends LecturerEntity> getLecturerById(UUID id);
}
