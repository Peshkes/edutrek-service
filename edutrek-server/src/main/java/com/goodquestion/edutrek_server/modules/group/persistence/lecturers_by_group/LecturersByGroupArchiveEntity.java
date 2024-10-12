package com.goodquestion.edutrek_server.modules.group.persistence.lecturers_by_group;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@Table(name = "lecturers_by_group_archive")
@NoArgsConstructor
public class LecturersByGroupArchiveEntity extends LecturersByGroupEntity {
    public LecturersByGroupArchiveEntity(LecturersByGroupEntity lecturersByGroupEntity) {
        super(lecturersByGroupEntity);
    }

    public LecturersByGroupArchiveEntity(UUID groupId, UUID lecturerId, boolean isWebinarist) {
        super(groupId, lecturerId, isWebinarist);
    }
}
