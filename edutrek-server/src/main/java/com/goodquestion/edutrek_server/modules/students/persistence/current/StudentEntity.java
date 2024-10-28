package com.goodquestion.edutrek_server.modules.students.persistence.current;


import com.goodquestion.edutrek_server.modules.contacts.persistence.AbstractContacts;
import com.goodquestion.edutrek_server.modules.students.dto.StudentsDataDto;
import com.goodquestion.edutrek_server.modules.students.dto.StudentsFromContactDataDto;
import com.goodquestion.edutrek_server.modules.students.persistence.AbstractStudent;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(schema = "current", name = "students")
public class StudentEntity extends AbstractStudent {

    public StudentEntity(StudentsDataDto studentsDataDto, int statusId) {
        super(studentsDataDto,statusId);
    }

    public StudentEntity(AbstractContacts contactData, StudentsFromContactDataDto studentsDataDto) {
        super(contactData, studentsDataDto);
    }
}
