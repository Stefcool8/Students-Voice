package com.example.studentsvoice.core.dto.Evaluation;

import com.example.studentsvoice.core.type.ActivityName;
import com.example.studentsvoice.core.type.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationTeacherDTO {
    private Long id;
    private ActivityName activityName;
    private ActivityType activityType;
    private int grade;
    private String comment;
    private String timestamp;
}

