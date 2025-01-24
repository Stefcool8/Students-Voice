package com.example.studentsvoice.evaluationservice.handler;

import com.example.studentsvoice.core.event.EvaluationCreatedEvent;
import com.example.studentsvoice.evaluationservice.entity.Evaluation;
import com.example.studentsvoice.evaluationservice.service.EvaluationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EvaluationEventHandler {

    @Inject
    private EvaluationService evaluationService;

    public void onEvaluationCreatedEvent(EvaluationCreatedEvent event) {
        Evaluation evaluation = event.getEvaluation();
        evaluationService.save(evaluation);
    }
}
