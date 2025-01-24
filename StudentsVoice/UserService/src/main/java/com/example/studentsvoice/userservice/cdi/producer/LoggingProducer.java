package com.example.studentsvoice.userservice.cdi.producer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;

import java.util.logging.Logger;

@ApplicationScoped
public class LoggingProducer {

    @Produces
    @MyLogger
    public Logger produceLogger(InjectionPoint ip) {
        return Logger.getLogger(ip.getMember().getDeclaringClass().getName());
    }
}

