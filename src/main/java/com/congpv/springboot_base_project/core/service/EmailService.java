package com.congpv.springboot_base_project.core.service;

public interface EmailService {
    public void sendNewTaskNotification(String toEmail, String taskTitle, String assigneeName);
}
