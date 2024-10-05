package com.goodquestion.edutrek_server.modules.group.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse {
    private List<GroupDto> groups;
    private int total;
    private int page;
    private int size;
}
