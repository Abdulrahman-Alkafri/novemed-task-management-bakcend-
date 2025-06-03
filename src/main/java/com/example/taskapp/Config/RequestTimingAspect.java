package com.example.taskapp.Config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RequestTimingAspect {

    private static final Logger logger = LoggerFactory.getLogger(RequestTimingAspect.class);

    @Around("execution(* com.example.taskapp..*Controller.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object proceed = joinPoint.proceed(); // This executes the actual method

        long executionTime = System.currentTimeMillis() - startTime;

        logger.info("{} executed in {} ms",
                joinPoint.getSignature(),
                executionTime);

        return proceed;
    }
}