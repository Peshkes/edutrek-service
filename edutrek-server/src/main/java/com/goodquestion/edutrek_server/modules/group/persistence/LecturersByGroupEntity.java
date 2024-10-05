package com.goodquestion.edutrek_server.modules.group.persistence;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "lecturers_by_group")
@IdClass(ComposeLecturerKey.class)
public class LecturersByGroupEntity {
    @Id
    @Column(name = "group_id")
    private UUID groupId;
    @Id
    @Column(name = "lecturer_id")
    private int weekdayId;
}
