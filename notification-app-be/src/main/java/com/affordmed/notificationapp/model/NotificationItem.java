package com.affordmed.notificationapp.model;

import lombok.Data;

@Data
public class NotificationItem {
    private String ID;
    private String Type;
    private String Message;
    private String Timestamp;
}