package com.congpv.springboot_base_project.core.service.impl;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.congpv.springboot_base_project.core.service.EmailService;

import java.io.File;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public void sendNewTaskNotification(String toEmail, String taskTitle, String assigneeName) {
        log.info("Bắt đầu tiến trình gửi email ngầm tới: {}", toEmail);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("congpv24@gmail.com");
            message.setTo(toEmail);
            message.setSubject("🔔 Thông báo: Bạn có Task mới!");
            message.setText("Chào " + assigneeName + ",\n\n" +
                    "Bạn vừa được gán một công việc mới: [" + taskTitle + "].\n" +
                    "Vui lòng đăng nhập hệ thống để xem chi tiết.\n\n" +
                    "Trân trọng,\nMini Jira System.");

            mailSender.send(message);

            log.info("Gửi email thành công tới: {}", toEmail);
        } catch (Exception e) {
            log.error("Lỗi khi gửi email tới {}: {}", toEmail, e.getMessage());
        }
    }

    @Override
    public void sendEmailWithAttachment(String email, String title, String subject, File generatedFile) {
        log.info("Start sending mail to: {}", email);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("congpv24@gmail.com");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(title);

            if (generatedFile != null && generatedFile.exists()) {
                helper.addAttachment(generatedFile.getName(), generatedFile);
            }

            mailSender.send(message);

            log.info("Send mail successfully with attachment to: {}", email);
        } catch (Exception e) {
            log.error("Error when sending email to {}: {}", email, e.getMessage());
        }
    }
}