package com.example.studentsvoice.userservice.cdi.interceptor;

import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import com.example.studentsvoice.userservice.cdi.producer.MyLogger;

import java.util.logging.Logger;

@Interceptor
@Loggable
public class LoggingInterceptor {
    @Inject
    @MyLogger
    private Logger LOGGER;

    @AroundInvoke
    public Object logMethodInvocation(InvocationContext context) throws Exception {
        long startTime = System.currentTimeMillis();
        try {
            LOGGER.info(() -> "Invoking method: " + context.getMethod().getName());
            return context.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            LOGGER.info(() -> "Method " + context.getMethod().getName() +
                    " executed in " + (endTime - startTime) + " ms");
        }
    }
}

