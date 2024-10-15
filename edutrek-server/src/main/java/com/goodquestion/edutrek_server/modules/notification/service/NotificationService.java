package com.goodquestion.edutrek_server.modules.notification.service;


import com.goodquestion.edutrek_server.modules.notification.persistence.NotificationDocument;
import com.goodquestion.edutrek_server.modules.notification.persistence.NotificationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.goodquestion.edutrek_server.error.ShareException.StatusNotFoundException;


@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationsRepository repository;

    public List<NotificationDocument> getAll() {
        return repository.findAll();
    }

    public NotificationDocument getById(int notificationId) {
        return repository.findById(notificationId).orElseThrow(() -> new StatusNotFoundException(notificationId));
    }
}
