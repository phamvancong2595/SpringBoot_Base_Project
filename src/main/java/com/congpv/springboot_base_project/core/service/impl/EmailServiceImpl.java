package com.congpv.springboot_base_project.core.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.congpv.springboot_base_project.core.service.EmailService;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Async("emailTaskExecutor")
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
            // Tại đây bạn có thể cấu hình bắn lỗi lên Sentry nếu email thất bại
        }
    }
}