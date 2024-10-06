package com.goodquestion.edutrek_server.modules.ContactsArchive.persistence;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;


@Document("contacts_archive")
public record ContactArchiveEntity(
        @Id
        UUID contactId,
        String contactName,
        String phone,
        String email,
        int statusId,
        int branchId,
        int courseId,
        String reasonOfArchivation,
        LocalDate archivationDate
) {


}
