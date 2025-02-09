package org.example.studentsvoice.dto.Evaluation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationStudentResponseDTO {
    private Long id;
    private String teacherName;
    private String activityName;
    private String activityType;
    private int grade;
    private String comment;
    private String timestamp;
}
