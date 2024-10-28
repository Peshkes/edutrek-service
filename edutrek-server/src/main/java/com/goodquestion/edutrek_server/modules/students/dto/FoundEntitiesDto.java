package com.goodquestion.edutrek_server.modules.students.dto;

import com.goodquestion.edutrek_server.modules.contacts.persistence.AbstractContacts;
import com.goodquestion.edutrek_server.modules.students.persistence.AbstractStudent;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
public class FoundEntitiesDto {
    private List<AbstractContacts> foundContacts;
    private List<AbstractStudent> foundStudents;

    public List<AbstractContacts> getFoundContacts() {
        return new ArrayList<>(foundContacts);
    }

    public List<AbstractStudent> getFoundStudents() {
        return new ArrayList<>(foundStudents);
    }


}
