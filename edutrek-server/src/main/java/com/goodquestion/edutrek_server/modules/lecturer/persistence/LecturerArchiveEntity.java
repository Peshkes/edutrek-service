package com.goodquestion.edutrek_server.modules.lecturer.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "lecturers_archive")
@NoArgsConstructor
public class LecturerArchiveEntity extends LecturerEntity {
    @Column(name = "reason_of_archivation")
    private String reasonOfArchivation;
    @Column(name = "archivation_date")
    private LocalDate archivationDate;

    public LecturerArchiveEntity(LecturerEntity lecturer, String reasonOfArchivation) {
        super(lecturer);
        this.reasonOfArchivation = reasonOfArchivation;
        this.archivationDate = LocalDate.now();
    }
}
