package com.goodquestion.edutrek_server.modules.group.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateGroupDto {
    private String groupName;
    private LocalDate finishDate;
    private Boolean isActive;
    private UUID courseId;
    private String slackLink;
    private String whatsAppLink;
    private String skypeLink;
    private Boolean deactivateAfter;
    private int[] lessons;
    private int[] webinars;
}