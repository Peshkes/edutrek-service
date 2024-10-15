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


    public StudentEntity(String contactName, String phone, String email, int statusId, int branchId, UUID targetCourseId, String comment, int fullPayment, boolean documentsDone) {
        super(contactName, phone, email, statusId, branchId, targetCourseId, comment, fullPayment, documentsDone);
    }

    public StudentEntity(StudentEntity studentEntity) {
        super(studentEntity);
    }

    public StudentEntity(StudentsDataDto studentsDataDto) {
        super(studentsDataDto);
    }
}
