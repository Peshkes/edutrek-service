package com.goodquestion.edutrek_server.modules.contacts.dto;


import com.goodquestion.edutrek_server.modules.contacts.persistence.AbstractContacts;

import java.util.List;

public record ContactSearchDto(List<AbstractContacts> contacts, int page, int pageSIze, long totalItems) {}
