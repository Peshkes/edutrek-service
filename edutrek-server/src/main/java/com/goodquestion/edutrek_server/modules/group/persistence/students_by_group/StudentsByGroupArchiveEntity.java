package com.goodquestion.edutrek_server.modules.group.persistence.students_by_group;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "students_by_group_archive")
public class StudentsByGroupArchiveEntity extends StudentsByGroupEntity {
    public StudentsByGroupArchiveEntity(StudentsByGroupEntity studentsByGroupEntity) {
        super(studentsByGroupEntity);
    }
}
