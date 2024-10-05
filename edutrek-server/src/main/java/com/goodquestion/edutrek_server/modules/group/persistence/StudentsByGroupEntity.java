package com.goodquestion.edutrek_server.modules.group.persistence;

import com.goodquestion.edutrek_server.modules.group.key.ComposeStudentsKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "students_by_group")
@IdClass(ComposeStudentsKey.class)
public class StudentsByGroupEntity {
    @Id
    @Column(name = "group_id")
    private UUID groupId;
    @Id
    @Column(name = "contact_id")
    private UUID studentId;
    @Setter
    @Column(name = "is_active")
    private Boolean isActive;
}
