package com.goodquestion.edutrek_server.modules.group.persistence.lecturers_by_group;

import com.goodquestion.edutrek_server.modules.group.key.ComposeLecturerKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@Table(name = "lecturers_by_group")
@IdClass(ComposeLecturerKey.class)
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class LecturersByGroupEntity {
    @Id
    @Column(name = "group_id")
    private UUID groupId;
    @Id
    @Column(name = "lecturer_id")
    private UUID lecturerId;
    @Column(name = "is_webinarist")
    private boolean isWebinarist;

    public LecturersByGroupEntity(LecturersByGroupEntity lecturersByGroupEntity) {
        this.groupId = lecturersByGroupEntity.getGroupId();
        this.lecturerId = lecturersByGroupEntity.getLecturerId();
        this.isWebinarist = lecturersByGroupEntity.isWebinarist();
    }
}
