package com.goodquestion.edutrek_server.modules.notification.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationsRepository extends MongoRepository<NotificationDocument, Integer> {

}