package com.goodquestion.edutrek_server.modules.lecturer.dto;

import com.goodquestion.edutrek_server.modules.lecturer.persistence.BaseLecturer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LecturerPaginationResponseDto {
    private List<BaseLecturer> lecturers;
    private long totalElements;
    private int page;
    private int size;
}
