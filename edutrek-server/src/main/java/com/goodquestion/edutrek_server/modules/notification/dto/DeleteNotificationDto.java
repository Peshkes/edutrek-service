package com.goodquestion.edutrek_server.modules.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteNotificationDto {
   private UUID entityId;
   private Integer[] notificationId;
}
