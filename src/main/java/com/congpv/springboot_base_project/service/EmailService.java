package com.congpv.springboot_base_project.service;

public interface EmailService {
    public void sendNewTaskNotification(String toEmail, String taskTitle, String assigneeName);
}
