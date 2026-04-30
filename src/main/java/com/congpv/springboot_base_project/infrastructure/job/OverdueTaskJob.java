package com.congpv.springboot_base_project.infrastructure.job;

import com.congpv.springboot_base_project.core.service.TaskService;
import com.congpv.springboot_base_project.infrastructure.messaging.RabbitMQProducer;
import com.congpv.springboot_base_project.shared.constant.ApplicationConstants;
import com.congpv.springboot_base_project.shared.dto.task.TaskResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class OverdueTaskJob {

    private final TaskService taskService;
    private final RedissonClient redissonClient;
    private final RabbitMQProducer rabbitMQProducer;

    @Value("${server.port}")
    private String serverPort;

    /**
     * Cấu hình Cron: Giây - Phút - Giờ - Ngày trong tháng - Tháng - Ngày trong tuần
     * "0 0 0 * * ?" -> Chạy vào đúng 00:00:00 (nửa đêm) mỗi ngày
     * "0 * * * * ?" -> Chạy mỗi phút 1 lần
     */
    @Scheduled(cron = "0 * * * * ?")
    public void scanAndNotifyOverdueTasks() {
        String lockKey = ApplicationConstants.CRON_OVERDUE_KEY;
        RLock lock = redissonClient.getLock(lockKey);
        boolean isLocked = false;

        try {
            isLocked = lock.tryLock(5, -1, TimeUnit.SECONDS);
            if (isLocked) {
                log.info("--- BẮT ĐẦU CRON JOB QUÉT TASK QUÁ HẠN ---");

                LocalDateTime now = LocalDateTime.now();
                List<TaskResponseDto> overdueTasks = taskService.getOverdueTasks(now.toLocalDate());

                if (overdueTasks.isEmpty()) {
                    log.info("Không có task nào quá hạn tại thời điểm này.");
                    return;
                }
                Thread.sleep(5000);
                log.warn("CẢNH BÁO: Phát hiện {} task đã quá hạn!", overdueTasks.size());
                for (TaskResponseDto task : overdueTasks) {
//                    rabbitMQProducer.publishTaskCreated(new TaskEvent(task.assigneeUsername(),
//                            task.title(), "congpv24@gmail.com"));
                    log.info("Xử lý Task ID: {} - Title: {} - Người chịu trách nhiệm: {}",
                            task.id(), task.title(),
                            task.assigneeUsername() != null ? task.assigneeUsername() : "Chưa gán");
                }

                log.info("--- KẾT THÚC CRON JOB ---");
                log.info("[Server Port: {}] ✅ End over due task job!", serverPort);
            } else {
                log.warn("⚠️ Another server is currently processing Task {}. Aborting current operation.", lockKey);
            }

        } catch (InterruptedException e) {
            log.error("❌ Thread interrupted while waiting to acquire Redis lock", e);
            Thread.currentThread().interrupt(); // Restore interrupted status

        } finally {
            if (isLocked && lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("🔓 Successfully released lock for Task {}.", lockKey);
            }
        }

    }
}
