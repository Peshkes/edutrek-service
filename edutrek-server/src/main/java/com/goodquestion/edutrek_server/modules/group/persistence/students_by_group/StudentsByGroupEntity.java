package com.goodquestion.edutrek_server.modules.group.persistence.students_by_group;

import com.goodquestion.edutrek_server.modules.group.key.ComposeStudentsKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "students_by_group")
@IdClass(ComposeStudentsKey.class)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class StudentsByGroupEntity {
    @Id
    @Column(name = "group_id")
    private UUID groupId;
    @Id
    @Column(name = "student_num")
    private UUID studentId;
    @Setter
    @Column(name = "is_active")
    private Boolean isActive;

    public StudentsByGroupEntity(StudentsByGroupEntity studentsByGroupEntity) {
        this.groupId = studentsByGroupEntity.getGroupId();
        this.studentId = studentsByGroupEntity.getStudentId();
        this.isActive = studentsByGroupEntity.getIsActive();
    }
}
