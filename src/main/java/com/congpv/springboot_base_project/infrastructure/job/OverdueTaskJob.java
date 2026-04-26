package com.congpv.springboot_base_project.infrastructure.job;

import com.congpv.springboot_base_project.core.entity.Task;
import com.congpv.springboot_base_project.infrastructure.repository.TaskRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OverdueTaskJob {

    private final TaskRepository taskRepository;

    // private final EmailService emailService;
    /**
     * Cấu hình Cron: Giây - Phút - Giờ - Ngày trong tháng - Tháng - Ngày trong tuần
     * "0 0 0 * * ?" -> Chạy vào đúng 00:00:00 (nửa đêm) mỗi ngày
     * "0 * * * * ?" -> Chạy mỗi phút 1 lần
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional(readOnly = true)
    public void scanAndNotifyOverdueTasks() {
        log.info("--- BẮT ĐẦU CRON JOB QUÉT TASK QUÁ HẠN ---");

        LocalDateTime now = LocalDateTime.now();
        List<Task> overdueTasks = taskRepository.findOverdueTasks(now);

        if (overdueTasks.isEmpty()) {
            log.info("Không có task nào quá hạn tại thời điểm này.");
            return;
        }

        log.warn("CẢNH BÁO: Phát hiện {} task đã quá hạn!", overdueTasks.size());

        for (Task task : overdueTasks) {
            log.info("Xử lý Task ID: {} - Title: {} - Người chịu trách nhiệm: {}",
                    task.getId(), task.getTitle(),
                    task.getAssignee() != null ? task.getAssignee().getUsername() : "Chưa gán");
        }

        log.info("--- KẾT THÚC CRON JOB ---");
    }
}
