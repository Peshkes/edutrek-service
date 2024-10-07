package com.goodquestion.edutrek_server.modules.contacts.archive;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;


@Document("contacts_archive")
public record ContactArchiveDocument(
        @Id
        UUID contactId,
        String contactName,
        String phone,
        String email,
        int statusId,
        int branchId,
        UUID courseId,
        String reasonOfArchivation,
        LocalDate archivationDate
) {


}
