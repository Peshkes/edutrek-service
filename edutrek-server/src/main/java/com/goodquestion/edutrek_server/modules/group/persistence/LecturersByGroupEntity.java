package com.goodquestion.edutrek_server.modules.group.persistence;

import com.goodquestion.edutrek_server.modules.group.key.ComposeLecturerKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "lecturers_by_group")
@IdClass(ComposeLecturerKey.class)
@AllArgsConstructor
@NoArgsConstructor
public class LecturersByGroupEntity {
    @Id
    @Column(name = "group_id")
    private UUID groupId;
    @Id
    @Column(name = "lecturer_id")
    private UUID lecturerId;
    @Column(name = "is_webinarist")
    private boolean isWebinarist;
}
