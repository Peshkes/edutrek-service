package com.goodquestion.edutrek_server.modules.group.dto;

import com.goodquestion.edutrek_server.modules.group.persistence.groups.GroupEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationGroupResponse {
    private List<? extends GroupEntity> groups;
    private long total;
    private int page;
    private int size;
}
