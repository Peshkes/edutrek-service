package com.goodquestion.edutrek_server.modules.studentInformation.dto;

import com.goodquestion.edutrek_server.modules.contacts.dto.ContactsDataDto;

public record StudentsAddDto(ContactsDataDto contactData, StudentsInfoDataDto studentsInfoData) {
}
