package com.example.studentsvoice.evaluationservice.aggregate;

import com.example.studentsvoice.core.command.CreateEvaluationCommand;
import com.example.studentsvoice.core.event.EvaluationCreatedEvent;
import com.example.studentsvoice.core.type.ActivityName;
import com.example.studentsvoice.core.type.ActivityType;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateRoot;


import java.time.LocalDateTime;
import java.util.UUID;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@AggregateRoot
public class EvaluationAggregate {
    @AggregateIdentifier
    private UUID registrationNumber;
    private String teacherId;
    private String studentId;
    private ActivityName activityName;
    private ActivityType activityType;
    private int grade;
    private String comment;
    private LocalDateTime timestamp;

    public EvaluationAggregate() {
        // Default constructor for Axon framework
    }

    @CommandHandler
    public EvaluationAggregate(CreateEvaluationCommand command) {
        if (command.getGrade() < 1 || command.getGrade() > 10) {
            throw new IllegalArgumentException("Grade must be between 1 and 10.");
        }

        apply(new EvaluationCreatedEvent(
                command.getRegistrationNumber(),
                command.getTeacherId(),
                command.getStudentId(),
                command.getActivityName(),
                command.getActivityType(),
                command.getGrade(),
                command.getComment(),
                LocalDateTime.now()
        ));
    }

    @EventHandler
    public void on(EvaluationCreatedEvent event) {
        this.registrationNumber = event.getRegistrationNumber();
        this.teacherId = event.getTeacherId();
        this.studentId = event.getStudentId();
        this.activityName = event.getActivityName();
        this.activityType = event.getActivityType();
        this.grade = event.getGrade();
        this.comment = event.getComment();
        this.timestamp = event.getTimestamp();
    }
}
