package com.goodquestion.edutrek_server.modules.group.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddGroupDto {
    @NotBlank(message = "Group name cannot be empty")
    @Size(max = 100, message = "Group name must not exceed 100 characters")
    private String groupName;

    @NotNull(message = "Start date cannot be null")
    @PastOrPresent(message = "Start date must be in the past or present")
    private LocalDate startDate;

    @NotNull(message = "Finish date cannot be null")
    private LocalDate finishDate;

    @NotNull(message = "Activity status cannot be null")
    private Boolean isActive;

    @NotNull(message = "Course ID cannot be null")
    private UUID courseId;

    @Pattern(regexp = "^https?://.*$", message = "Slack link must be a valid URL")
    private String slackLink;

    @Pattern(regexp = "^https?://.*$", message = "WhatsApp link must be a valid URL")
    private String whatsAppLink;

    @Pattern(regexp = "^https?://.*$", message = "Skype link must be a valid URL")
    private String skypeLink;

    @NotNull(message = "Deactivate after cannot be null")
    private Boolean deactivateAfter;
}