package com.sg.shopping.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLogAspect {

    public static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

    @Around("execution(* com.sg.shopping.service.impl..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("====== Start {}.{} ======",
                joinPoint.getTarget().getClass(),
                joinPoint.getSignature().getName());

        long startTimestamp = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTimestamp = System.currentTimeMillis();

        long interval = endTimestamp - startTimestamp;
        if (interval > 3000) {
            logger.error("====== End, took {} ms ======", interval);
        } else if (interval > 2000) {
            logger.warn("====== End, took {} ms ======", interval);
        } else {
            logger.info("====== End, took {} ms ======", interval);
        }

        return result;

    }

}
