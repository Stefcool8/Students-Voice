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
public class EvaluationAdminDTO {
    private Long id;
    private String registrationNumber;
    private String teacherName;
    private String studentName;
    private ActivityName activityName;
    private ActivityType activityType;
    private Integer grade;
    private String comment;
    private String timestamp;
}
