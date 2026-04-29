package com.congpv.springboot_base_project.core.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisLockTemplate {
    private final RedissonClient redissonClient;

    public void processCriticalTask(Long taskId) {
        // 1. Define the lock key (Granular lock per Task ID to avoid blocking other tasks)
        String lockKey = "lock:task:" + taskId;
        RLock lock = redissonClient.getLock(lockKey);

        boolean isLocked = false;

        try {
            // 2. Attempt to acquire the lock
            // waitTime = 5s: Wait up to 5 seconds to acquire the lock. Give up if exceeded.
            // leaseTime = -1: REDISSON'S MAGIC SPELL. Set to -1 to activate the Watchdog mechanism.
            isLocked = lock.tryLock(5, -1, TimeUnit.SECONDS);

            if (isLocked) {
                log.info("🔐 Successfully acquired lock for Task {}. Starting execution...", taskId);

                // --- START BUSINESS LOGIC ---
                // Simulate a time-consuming process
                Thread.sleep(8000);
                // --- END BUSINESS LOGIC ---

                log.info("✅ Successfully processed Task {}.", taskId);
            } else {
                log.warn("⚠️ Another server is currently processing Task {}. Aborting current operation.", taskId);
                // Throw an Exception or return false based on business requirements
            }

        } catch (InterruptedException e) {
            log.error("❌ Thread interrupted while waiting to acquire Redis lock", e);
            Thread.currentThread().interrupt(); // Restore interrupted status

        } finally {
            // 3. MANDATORY: ALWAYS RELEASE THE LOCK IN THE FINALLY BLOCK
            // Check if the lock is currently held AND if it is held by the CURRENT THREAD
            if (isLocked && lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("🔓 Successfully released lock for Task {}.", taskId);
            }
        }
    }
}

