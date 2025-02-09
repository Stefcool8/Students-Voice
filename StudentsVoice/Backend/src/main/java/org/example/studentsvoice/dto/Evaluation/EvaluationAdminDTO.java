package org.example.studentsvoice.dto.Evaluation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationAdminDTO {
    private Long id;
    private String registrationNumber;
    private String teacherName;
    private String studentName;
    private String activityName;
    private String activityType;
    private Integer grade;
    private String comment;
    private String timestamp;
}
