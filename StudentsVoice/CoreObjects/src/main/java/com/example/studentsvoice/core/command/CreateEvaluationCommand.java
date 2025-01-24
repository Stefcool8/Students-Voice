package com.example.studentsvoice.core.command;

import com.example.studentsvoice.core.type.ActivityName;
import com.example.studentsvoice.core.type.ActivityType;
import lombok.Builder;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Value
@Builder
public class CreateEvaluationCommand {
    @TargetAggregateIdentifier
    UUID registrationNumber;
    String teacherName;
    String studentUsername;
    ActivityName activityName;
    ActivityType activityType;
    int grade;
    String comment;
}
