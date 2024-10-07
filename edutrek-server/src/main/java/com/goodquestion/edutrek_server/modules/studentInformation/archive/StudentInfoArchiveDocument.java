package com.goodquestion.edutrek_server.modules.studentInformation.archive;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;


@Document("students_archive")
public record StudentInfoArchiveDocument(
        @Id
        UUID contactId,
        int fullPayment,
        boolean documentsDone,
        LocalDate dateOfArchivation,
        String reasonOfArchivation
) {


}
