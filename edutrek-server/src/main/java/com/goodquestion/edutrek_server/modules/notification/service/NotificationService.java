package com.goodquestion.edutrek_server.modules.notification.service;


import com.goodquestion.edutrek_server.error.DatabaseException;
import com.goodquestion.edutrek_server.modules.notification.dto.NotificationDataDto;
import com.goodquestion.edutrek_server.modules.notification.dto.NotificationDto;
import com.goodquestion.edutrek_server.modules.notification.persistence.NotificationDocument;
import com.goodquestion.edutrek_server.modules.notification.persistence.NotificationsRepository;
import com.goodquestion.edutrek_server.utility_service.logging.Loggable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.goodquestion.edutrek_server.error.ShareException.*;


@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationsRepository repository;

//    @Loggable
//    public List<NotificationDocument> getAllEntityNotifications(UUID id) {
//       return repository.findAllNotificationsById(id);
//    }

    @Loggable
    public NotificationDocument getById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new NotificationNotFoundException(id.toString()));
    }

    @Loggable
    @Transactional
    public void addNotificationToId(UUID id, NotificationDto notificationDto) {
        NotificationDocument notification = repository.findById(id).orElse(null);
        if (notification != null) {
            List<NotificationDataDto> notificationsList = notification.getNotificationData();
            notificationsList.add(new NotificationDataDto(notificationsList.getLast().getNotificationId() + 1, notificationDto));
        } else {
            List<NotificationDataDto> newList = new ArrayList<>();
            newList.add(new NotificationDataDto(0, notificationDto));
            notification = new NotificationDocument(id, newList);
        }
        try {
            repository.save(notification);
        } catch (Exception e) {
            throw new DatabaseException.DatabaseAddingException(e.getMessage());
        }
    }

    @Loggable
    @Transactional
    public void deleteNotificationById(UUID entityId, int... elementNumbers) {
        if (!repository.existsById(entityId))
            throw new NotificationNotFoundException(entityId.toString());
        if (elementNumbers != null) {
            repository.deleteNotificationDocumentsById(entityId, elementNumbers);//TODO как быть с ошибками отсутствия такого элемента
        } else {
            try {
                repository.deleteById(entityId);
            } catch (Exception e) {
                throw new DatabaseException.DatabaseDeletingException(e.getMessage());
            }
        }
    }

    @Loggable
    @Transactional
    public void updateById(UUID id, @Valid NotificationDataDto notificationDataDto) {
        if (!repository.existsById(id))
            throw new NotificationNotFoundException(id.toString());
        try {
            repository.updateNotificationDocumentsByNotificationId(id, notificationDataDto.getNotificationId(), notificationDataDto.getNotificationDate(), notificationDataDto.getNotificationText());
        } catch (Exception e) {
            throw new DatabaseException.DatabaseDeletingException(e.getMessage());
        }
    }
}
