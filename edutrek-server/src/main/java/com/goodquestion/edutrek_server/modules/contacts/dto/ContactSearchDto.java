package com.goodquestion.edutrek_server.modules.contacts.dto;


import com.goodquestion.edutrek_server.modules.contacts.persistence.ContactsEntity;
import jakarta.validation.constraints.Size;

import java.util.List;

public class ContactSearchDto {
        List<ContactsEntity> contacts;

}
