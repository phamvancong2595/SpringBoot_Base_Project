package com.congpv.springboot_base_project.infrastructure.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAndPerformance {
    @Around("@within(org.springframework.web.bind.annotation.RestController) || " +
            "@within(org.springframework.stereotype.Service)"
    )
    public Object logAndMeasurePerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().toShortString();
        Object[] methodArgs = joinPoint.getArgs();
        log.info("➡️ Entering method: {}", methodName);
        log.info("📥 Arguments: {}", Arrays.toString(methodArgs));
        try {
            Object result = joinPoint.proceed();
            log.info("✅ Method executed successfully: {}", methodName);
            return result;
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            if (executionTime > 2000) {
                log.warn("🐢 Slow Performance Alert: {} took {} ms", methodName, executionTime);
            } else {
                log.info("⏱ Execution time: {} ms", executionTime);
            }
        }
    }
}
