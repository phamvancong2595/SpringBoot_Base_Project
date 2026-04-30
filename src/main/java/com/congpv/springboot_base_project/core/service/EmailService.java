package com.congpv.springboot_base_project.core.service;

import java.io.File;

public interface EmailService {
    public void sendNewTaskNotification(String toEmail, String taskTitle, String assigneeName);

    void sendEmailWithAttachment(String email, String title, String subject, File generatedFile);
}
