package com.goodquestion.edutrek_server.modules.notification.persistence;



import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor
@Document("notifications")
public class NotificationDocument {

    @Id
    private int weekdayId;

    private String notificationDate;

}