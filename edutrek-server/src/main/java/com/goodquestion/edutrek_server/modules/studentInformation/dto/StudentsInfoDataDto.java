package com.goodquestion.edutrek_server.modules.studentInformation.dto;


import com.goodquestion.edutrek_server.modules.contacts.dto.ContactsDataDto;
import lombok.*;

import java.util.UUID;


@NoArgsConstructor
@Getter
@Setter
public class StudentsInfoDataDto  extends ContactsDataDto {


    private int fullPayment;
    private boolean documentsDone;

    public StudentsInfoDataDto(String contactName, String phone, String email, int statusId, int branchId, UUID targetCourseId, String comment, int fullPayment, boolean documentsDone) {
        super(contactName, phone, email, statusId, branchId, targetCourseId, comment);

        this.fullPayment = fullPayment;
        this.documentsDone = documentsDone;
    }
}
