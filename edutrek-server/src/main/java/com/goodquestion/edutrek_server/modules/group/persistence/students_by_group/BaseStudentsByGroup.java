package com.goodquestion.edutrek_server.modules.group.persistence.students_by_group;

import com.goodquestion.edutrek_server.modules.group.key.ComposeStudentsKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@IdClass(ComposeStudentsKey.class)
@MappedSuperclass
public class BaseStudentsByGroup {
    @Id
    @Column(name = "group_id")
    private UUID groupId;
    @Id
    @Column(name = "student_id")
    private UUID studentId;
    @Setter
    @Column(name = "is_active")
    private Boolean isActive;

    public BaseStudentsByGroup(BaseStudentsByGroup studentsByGroupEntity) {
        this.groupId = studentsByGroupEntity.getGroupId();
        this.studentId = studentsByGroupEntity.getStudentId();
        this.isActive = studentsByGroupEntity.getIsActive();
    }
}
