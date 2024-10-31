package com.goodquestion.edutrek_server.modules.notification.service;


import com.goodquestion.edutrek_server.error.DatabaseException;
import com.goodquestion.edutrek_server.modules.authentication.service.AuthenticationBaseService;
import com.goodquestion.edutrek_server.modules.notification.dto.NotificationDataDto;
import com.goodquestion.edutrek_server.modules.notification.dto.NotificationDto;
import com.goodquestion.edutrek_server.modules.notification.events.SseService;
import com.goodquestion.edutrek_server.modules.notification.persistence.NotificationDocument;
import com.goodquestion.edutrek_server.modules.notification.persistence.NotificationsRepository;
import com.goodquestion.edutrek_server.utility_service.logging.Loggable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.goodquestion.edutrek_server.error.ShareException.NotificationNotFoundException;


@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationsRepository repository;
    private final SseService sseService;

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
    public void addNotificationToId(UUID entityId, NotificationDto notificationDto) {

        NotificationDocument notification = repository.findById(entityId).orElse(null);
        NotificationDataDto notificationDataDto;
        if (notification != null) {
            List<NotificationDataDto> notificationsList = notification.getNotificationData();
            notificationDataDto = new NotificationDataDto(notificationsList.getLast().getNotificationId() + 1, notificationDto);
            notificationsList.add(notificationDataDto);
        } else {
            List<NotificationDataDto> newList = new ArrayList<>();
            System.out.println(notificationDto);
            notificationDataDto = new NotificationDataDto(0, notificationDto);
            newList.add(notificationDataDto);
            notification = new NotificationDocument(entityId, newList);
        }
        try {
            repository.save(notification);
        } catch (Exception e) {
            throw new DatabaseException.DatabaseAddingException(e.getMessage());
        }
    }

    @Loggable
    @Transactional
    public void deleteNotificationById(UUID entityId, Integer[] elementNumbers) {
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
            repository.updateNotificationDocumentsByNotificationId(id, notificationDataDto.getNotificationId(), notificationDataDto.getScheduledTime(), notificationDataDto.getNotificationText());
        } catch (Exception e) {
            throw new DatabaseException.DatabaseDeletingException(e.getMessage());
        }
    }

    @Loggable
    @Scheduled(fixedRate = 60000)
    public void sendScheduledNotifications() {
        Map<UUID, List<NotificationDataDto>> mapOfDocs = repository.findAll().stream().collect(Collectors.toMap(NotificationDocument::getId,
                NotificationDocument::getNotificationData));
        mapOfDocs.entrySet().removeIf(entry -> {
            entry.getValue().removeIf(value -> value.getScheduledTime().isAfter(LocalDateTime.now()));
            return entry.getValue().isEmpty();
        });
        sseService.sendMessages(mapOfDocs);
    }
}
