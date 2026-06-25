package com.affordmed.notificationapp.service;

import com.affordmed.notificationapp.dto.NotificationResponse;
import com.affordmed.notificationapp.model.NotificationItem;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class NotificationService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String NOTIFICATIONS_API =
            "http://4.224.186.213/evaluation-service/notifications";

    private static final String ACCESS_TOKEN =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJNYXBDbGFpbXMiOnsiYXVkIjoiaHR0cDovLzIwLjI0NC41Ni4xNDQvZXZhbHVhdGlvbi1zZXJ2aWNlIiwiZW1haWwiOiIyMzIxMWE2Nmo2QGJ2cml0LmFjLmluIiwiZXhwIjoxNzgyMzgwNTY5LCJpYXQiOjE3ODIzNzk2NjksImlzcyI6IkFmZm9yZCBNZWRpY2FsIFRlY2hub2xvZ2llcyBQcml2YXRlIExpbWl0ZWQiLCJqdGkiOiI0MTdlMmJiYi1mYzc1LTQ2M2QtYTg2ZC0wZGY0MDJjMTY2YjYiLCJsb2NhbGUiOiJlbi1JTiIsIm5hbWUiOiJ2dXBwYWxhIHZhcnNoaXRoIiwic3ViIjoiNzVkODgzOWYtNjgzMC00ZTE3LTg4MWUtYTE1ZGUyNWMzYWE5In0sImVtYWlsIjoiMjMyMTFhNjZqNkBidnJpdC5hYy5pbiIsIm5hbWUiOiJ2dXBwYWxhIHZhcnNoaXRoIiwicm9sbE5vIjoiMjMyMTFhNjZqNiIsImFjY2Vzc0NvZGUiOiJhaFhqdnAiLCJjbGllbnRJRCI6Ijc1ZDg4MzlmLTY4MzAtNGUxNy04ODFlLWExNWRlMjVjM2FhOSIsImNsaWVudFNlY3JldCI6IkVoV1BhUFhaVFdQa2RqVksifQ.y22_8A67pnrwDA-N7OupIo08Ukt3QFWP-F_9A5Xu89U";

    public List<NotificationItem> getTop10Notifications() {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + ACCESS_TOKEN);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<NotificationResponse> response =
                restTemplate.exchange(
                        NOTIFICATIONS_API,
                        HttpMethod.GET,
                        request,
                        NotificationResponse.class
                );

        List<NotificationItem> notifications = response.getBody().getNotifications();

        notifications.sort(
                Comparator
                        .comparingInt((NotificationItem n) -> getPriority(n.getType()))
                        .reversed()
                        .thenComparing(
                                n -> LocalDateTime.parse(n.getTimestamp().replace(" ", "T")),
                                Comparator.reverseOrder()
                        )
        );

        return notifications.stream()
                .limit(10)
                .toList();
    }

    private int getPriority(String type) {
        return switch (type.toLowerCase()) {
            case "placement" -> 3;
            case "result" -> 2;
            case "event" -> 1;
            default -> 0;
        };
    }
}