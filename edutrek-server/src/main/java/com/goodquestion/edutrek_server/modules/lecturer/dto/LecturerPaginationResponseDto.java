package com.goodquestion.edutrek_server.modules.lecturer.dto;

import com.goodquestion.edutrek_server.modules.lecturer.persistence.LecturerEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LecturerPaginationResponseDto {
    private List<LecturerEntity> lecturers;
    private long totalElements;
    private int page;
    private int size;
}
