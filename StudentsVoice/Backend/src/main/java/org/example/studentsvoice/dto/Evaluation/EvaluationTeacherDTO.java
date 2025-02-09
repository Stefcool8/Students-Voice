package org.example.studentsvoice.dto.Evaluation;

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
    private String activityName;
    private String activityType;
    private int grade;
    private String comment;
    private String timestamp;
}

