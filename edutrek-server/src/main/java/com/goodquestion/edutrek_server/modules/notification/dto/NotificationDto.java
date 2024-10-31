package com.goodquestion.edutrek_server.modules.notification.dto;




import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private UUID recipientId;
    private LocalDateTime scheduledTime;
    private String notificationText;
}