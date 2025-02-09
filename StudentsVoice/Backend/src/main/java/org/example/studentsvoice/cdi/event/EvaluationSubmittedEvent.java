package org.example.studentsvoice.cdi.event;

import org.example.studentsvoice.entity.Evaluation;

public record EvaluationSubmittedEvent(Evaluation evaluation) {
}
