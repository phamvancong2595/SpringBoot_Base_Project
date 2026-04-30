package com.congpv.springboot_base_project.infrastructure.aspect;

import com.congpv.springboot_base_project.infrastructure.config.data_source.DataSourceContextHolder;
import com.congpv.springboot_base_project.shared.enums.DataSourceType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@Slf4j
public class DataSourceAspect {
    @Pointcut("execution(* com.congpv.springboot_base_project.core.service..*.*(..))")
    public void serviceMethods() {}

    @Pointcut("execution(* com.congpv.springboot_base_project.core.service..*.get*(..)) || " +
            "execution(* com.congpv.springboot_base_project.core.service..*.find*(..)) || " +
            "execution(* com.congpv.springboot_base_project.core.service..*.search*(..)) || " +
            "execution(* com.congpv.springboot_base_project.core.service..*.read*(..)) || " +
            "execution(* com.congpv.springboot_base_project.core.service..*.count*(..))")
    public void readOnlyMethods() {}

    @Before("serviceMethods() && readOnlyMethods()")
    public void setReadDataSource(JoinPoint joinPoint) {
        log.info("🔄 [AOP] Routing read function '{}' to: REPLICA (Slave)", joinPoint.getSignature().getName());
        DataSourceContextHolder.set(DataSourceType.REPLICA);
    }

    @Before("serviceMethods() && !readOnlyMethods()")
    public void setWriteDataSource(JoinPoint joinPoint) {
        log.info("🔄 [AOP] Routing write function '{}' to: PRIMARY (Master)", joinPoint.getSignature().getName());
        DataSourceContextHolder.set(DataSourceType.PRIMARY);
    }

    @After("serviceMethods()")
    public void clearDataSource(JoinPoint joinPoint) {
        DataSourceContextHolder.clear();
    }
}
