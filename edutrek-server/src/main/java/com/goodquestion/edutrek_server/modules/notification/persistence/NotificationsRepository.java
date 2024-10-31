package com.goodquestion.edutrek_server.modules.notification.persistence;

import com.goodquestion.edutrek_server.modules.notification.dto.NotificationDataDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationsRepository extends MongoRepository<NotificationDocument, UUID> {


    @Query("{ '_id': ?0}")
    @Update("{ '$pull' : { 'notificationData': {'notificationId': {$in: ?1}}  } } ")
    void deleteNotificationDocumentsById(UUID entityId, Integer[] elementNumbers);

    @Query(value = "{ '_id': ?0, 'notificationData.notificationId': ?1 }")
    @Update(value = "{ '$set': { 'notificationData.$.notificationDate': ?2, 'notificationData.$.notificationText': ?3 } }")
    void updateNotificationDocumentsByNotificationId(UUID entityId, int notificationId, LocalDateTime notificationDate, String notificationText);

    @Query("{ '_id': ?0}")
    List<NotificationDataDto> findByScheduledTimeBefore(UUID id, LocalDateTime time);
}