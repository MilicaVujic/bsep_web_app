package com.example.security.config;

import com.example.security.dto.NotificationMessageDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {
    //private static final Logger log = LoggerFactory.getLogger(UpdateController.class);


    @MessageMapping("/ws")
    @SendTo("/user/queue/notification")
    public NotificationMessageDto send(NotificationMessageDto dto) {
        return dto;
    }
}
