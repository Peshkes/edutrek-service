package com.goodquestion.edutrek_server.modules.students.dto;

import com.goodquestion.edutrek_server.modules.contacts.dto.ContactsDataDto;

public record StudentsAddDto(ContactsDataDto contactData, StudentsDataDto studentsInfoData) {
}
