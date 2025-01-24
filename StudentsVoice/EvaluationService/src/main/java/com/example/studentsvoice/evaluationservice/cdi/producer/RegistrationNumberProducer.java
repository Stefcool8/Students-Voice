package com.example.studentsvoice.evaluationservice.cdi.producer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

import java.util.UUID;
import java.util.logging.Logger;

@ApplicationScoped
public class RegistrationNumberProducer {
    @Inject
    @MyLogger
    private Logger LOGGER;

    @Produces
    @RegistrationNumber
    public UUID generateRegistrationNumber() {
        UUID newUuid = UUID.randomUUID();
        LOGGER.info("Generated registration number in producer: " + newUuid);
        return newUuid;
    }
}
