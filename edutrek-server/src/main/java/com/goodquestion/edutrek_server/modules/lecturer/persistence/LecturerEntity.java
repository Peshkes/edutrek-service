package com.goodquestion.edutrek_server.modules.lecturer.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(schema = "current", name = "lecturers")
@Getter
@NoArgsConstructor
public class LecturerEntity extends BaseLecturer {
    public LecturerEntity(String lecturerName, String phone, String email, int branchId, String comment) {
        super(lecturerName, phone, email, branchId, comment);
    }
}