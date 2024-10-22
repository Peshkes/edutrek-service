package com.goodquestion.edutrek_server.modules.students.persistence.current;


import com.goodquestion.edutrek_server.modules.students.dto.StudentsDataDto;
import com.goodquestion.edutrek_server.modules.students.persistence.AbstractStudent;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(schema = "current", name = "students")
public class StudentEntity extends AbstractStudent {

    public StudentEntity(StudentsDataDto studentsDataDto, int statusId) {
        super(studentsDataDto,statusId);
    }

    public StudentEntity(UUID id, StudentsDataDto studentsDataDto) {
        super(id, studentsDataDto);
    }
}
