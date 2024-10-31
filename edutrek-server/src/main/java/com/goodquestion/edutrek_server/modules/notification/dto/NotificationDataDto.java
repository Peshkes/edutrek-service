package com.goodquestion.edutrek_server.modules.notification.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@NoArgsConstructor
public class NotificationDataDto {
    private int notificationId;
    private UUID recipientId;
    private LocalDateTime scheduledTime;
    private String notificationText;

    public NotificationDataDto(int notificationId, NotificationDto notificationDto) {
        this.notificationId = notificationId;
        this.recipientId = notificationDto.getRecipientId();
        this.scheduledTime = notificationDto.getScheduledTime();
        this.notificationText = notificationDto.getNotificationText();
    }
}