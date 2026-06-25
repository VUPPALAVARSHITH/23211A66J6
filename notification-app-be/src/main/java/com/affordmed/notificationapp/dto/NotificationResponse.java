package com.affordmed.notificationapp.dto;

import com.affordmed.notificationapp.model.NotificationItem;
import lombok.Data;

import java.util.List;

@Data
public class NotificationResponse {
    private List<NotificationItem> notifications;
}