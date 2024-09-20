package com.example.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendNotificationToUser(String email, String message) {
        messagingTemplate.convertAndSendToUser(email, "/user/queue/notification/"+email, message);
    }
}

