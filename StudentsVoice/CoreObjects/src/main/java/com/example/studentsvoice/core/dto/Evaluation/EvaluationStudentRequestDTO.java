package com.example.studentsvoice.core.dto.Evaluation;

import com.example.studentsvoice.core.type.ActivityName;
import com.example.studentsvoice.core.type.ActivityType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationStudentRequestDTO {
    @NotBlank(message = "Teacher name cannot be blank.")
    @Size(max = 50, message = "Teacher name cannot exceed 50 characters.")
    private String teacherName;

    @NotBlank(message = "Activity name cannot be blank.")
    @Size(max = 50, message = "Activity name cannot exceed 50 characters.")
    private ActivityName activityName;

    @NotBlank(message = "Activity type cannot be blank.")
    @Size(max = 50, message = "Activity type cannot exceed 50 characters.")
    private ActivityType activityType;

    @Min(value = 1, message = "Grade must be at least 1.")
    @Max(value = 10, message = "Grade cannot be greater than 10.")
    private int grade;

    @Size(max = 500, message = "Comment cannot exceed 500 characters.")
    private String comment;
}
