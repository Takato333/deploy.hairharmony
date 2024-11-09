package com.example.demo.api;

import com.example.demo.model.NotificationFCM;
import com.example.demo.service.NotificationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
@CrossOrigin("*")
@SecurityRequirement(name = "api")

public class NotificationAPI {

    @Autowired
    NotificationService notificationService;

    @PostMapping
    public void sendNotification(@RequestBody NotificationFCM notificationFCM) {
        notificationService.sendNotification(notificationFCM);
    }

}
