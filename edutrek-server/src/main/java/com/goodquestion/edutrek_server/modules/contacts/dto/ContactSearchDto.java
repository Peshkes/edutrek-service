package com.goodquestion.edutrek_server.modules.contacts.dto;


import com.goodquestion.edutrek_server.modules.contacts.persistence.ContactsEntity;


import java.util.List;

public record ContactSearchDto(List<ContactsEntity> contacts, int page, int pageSIze, long totalItems) {}
