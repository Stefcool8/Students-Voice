package org.example.studentsvoice.cdi.observer;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.example.studentsvoice.cdi.event.EvaluationSubmittedEvent;
import org.example.studentsvoice.cdi.producer.MyLogger;

import java.util.logging.Logger;

@RequestScoped
public class EvaluationSubmittedObserver {

    @Inject
    @MyLogger
    private Logger LOGGER;

    public void onEvaluationSubmitted(@Observes EvaluationSubmittedEvent event) {
        LOGGER.info("Evaluation submitted successfully. Message from observer.");
    }
}
