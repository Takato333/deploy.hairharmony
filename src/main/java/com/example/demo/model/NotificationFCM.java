package com.example.demo.model;

import lombok.Data;

@Data
public class NotificationFCM {
    String title;
    String body;
    String fcmToken;
}
