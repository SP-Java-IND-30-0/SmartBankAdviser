package com.star.bank.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class LoggingAspect {

    @Before("execution(* com.star.bank.controller.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Calling method: {} with parameters: {} ", joinPoint.getSignature().getName(), joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "execution(* com.star.bank.controller.*.*(..))", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        log.info("Method {} completed successfully and returning result: {} ", joinPoint.getSignature().getName(), result);
    }

    @AfterThrowing(pointcut = "execution(* com.star.bank.controller.*.*(..))", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Exception exception) {
        log.error("Method {} threw an exception: {} ", joinPoint.getSignature().getName(), exception.getMessage());
    }
}
