package com.goodquestion.edutrek_server.modules.notification.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public class NotificationDataDto {
    private int notificationId;
    private LocalDateTime notificationDate;
    private String notificationText;

    public NotificationDataDto(int notificationId, NotificationDto notificationDto) {
        this.notificationId = notificationId;
        this.notificationDate = notificationDto.getNotificationDate();
        this.notificationText = notificationDto.getNotificationText();    }
}