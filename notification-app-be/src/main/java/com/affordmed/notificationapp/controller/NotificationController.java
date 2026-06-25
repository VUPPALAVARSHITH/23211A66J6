package com.affordmed.notificationapp.controller;

import com.affordmed.notificationapp.model.NotificationItem;
import com.affordmed.notificationapp.service.NotificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/notifications/top10")
    public List<NotificationItem> getTop10Notifications() {
        return notificationService.getTop10Notifications();
    }
}