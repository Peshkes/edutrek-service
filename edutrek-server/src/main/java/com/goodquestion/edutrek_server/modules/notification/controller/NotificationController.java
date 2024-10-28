package com.goodquestion.edutrek_server.modules.notification.controller;


import com.goodquestion.edutrek_server.modules.notification.dto.DeleteNotificationDto;
import com.goodquestion.edutrek_server.modules.notification.dto.NotificationDataDto;
import com.goodquestion.edutrek_server.modules.notification.dto.NotificationDto;
import com.goodquestion.edutrek_server.modules.notification.persistence.NotificationDocument;
import com.goodquestion.edutrek_server.modules.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;


@Validated
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

//    @GetMapping("/all/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public List<NotificationDocument> getAllEntityNotifications(@PathVariable UUID id) {
//        return service.getAllEntityNotifications(id);
//    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NotificationDocument getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> addNotificationToId(@PathVariable UUID id, @RequestBody @Valid NotificationDto notificationDto) {
       service.addNotificationToId(id, notificationDto);
       return new ResponseEntity<>("Notification created", HttpStatus.CREATED);
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteById( @RequestBody @Valid DeleteNotificationDto deleteNotificationDto) {
        service.deleteNotificationById(deleteNotificationDto.getEntityId(), deleteNotificationDto.getNotificationId());
        return new ResponseEntity<>("Notification deleted", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateById(@PathVariable UUID id, @RequestBody @Valid NotificationDataDto notificationDataDto) {
        service.updateById(id, notificationDataDto);
        return new ResponseEntity<>("Contact updated", HttpStatus.OK);
    }

}
