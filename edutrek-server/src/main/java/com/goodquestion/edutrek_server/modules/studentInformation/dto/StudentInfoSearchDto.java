package com.goodquestion.edutrek_server.modules.studentInformation.dto;



import com.goodquestion.edutrek_server.modules.studentInformation.persistence.StudentInfoEntity;

import java.util.List;

public record StudentInfoSearchDto(List<StudentInfoEntity> contacts, int page, int pageSIze, long totalItems) {}
