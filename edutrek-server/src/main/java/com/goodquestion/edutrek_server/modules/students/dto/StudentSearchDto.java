package com.goodquestion.edutrek_server.modules.students.dto;



import com.goodquestion.edutrek_server.modules.students.persistence.AbstractStudent;
import com.goodquestion.edutrek_server.modules.students.persistence.current.StudentEntity;

import java.util.List;

public record StudentSearchDto(List<AbstractStudent> students, int page, int pageSIze, long totalItems) {}