package com.example.studentsvoice.core.event;

import com.example.studentsvoice.core.type.ActivityName;
import com.example.studentsvoice.core.type.ActivityType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class EvaluationCreatedEvent {
    private UUID registrationNumber;
    private String teacherId;
    private String studentId;
    private ActivityName activityName;
    private ActivityType activityType;
    private int grade;
    private String comment;
    private LocalDateTime timestamp;
}
