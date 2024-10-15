package com.goodquestion.edutrek_server.modules.notification.controller;


import com.goodquestion.edutrek_server.modules.notification.persistence.NotificationDocument;
import com.goodquestion.edutrek_server.modules.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<NotificationDocument> getAllStatuses() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NotificationDocument getById(@PathVariable int id) {
            return service.getById(id);
    }
}
