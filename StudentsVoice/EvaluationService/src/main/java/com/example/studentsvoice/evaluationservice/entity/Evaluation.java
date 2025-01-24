package com.example.studentsvoice.evaluationservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import com.example.studentsvoice.core.type.ActivityType;
import com.example.studentsvoice.core.type.ActivityName;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "evaluations")
@NamedQueries({
        @NamedQuery(name = "Evaluation.findAll", query = "SELECT e FROM Evaluation e"),
        @NamedQuery(name = "Evaluation.findByTeacherId", query = "SELECT e FROM Evaluation e WHERE e.teacherId = :teacherId"),
        @NamedQuery(name = "Evaluation.findByStudentId", query = "SELECT e FROM Evaluation e WHERE e.studentId = :studentId"),
        @NamedQuery(name = "Evaluation.findByStudentTeacherActivity",
                query = "SELECT e FROM Evaluation e WHERE e.studentId = :studentId AND e.teacherId = :teacherId " +
                        "AND e.activityName = :activity_name AND e.activityType = :activity_type")
})
@ToString
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "registration_number", nullable = false, unique = true, updatable = false)
    private UUID registrationNumber;

    @Column(name = "teacher_id", nullable = false)
    @NotNull(message = "Teacher is required.")
    private String teacherId;

    @Column(name = "student_id", nullable = false)
    @NotNull(message = "Student is required.")
    private String studentId;

    @Column(name = "activity_name", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Activity name is required.")
    private ActivityName activityName;

    @Column(name = "activity_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Activity type is required.")
    private ActivityType activityType;

    @Column(nullable = false)
    @Min(value = 1, message = "Grade must be at least 1.")
    @Max(value = 10, message = "Grade cannot be greater than 10.")
    private Integer grade;

    @Size(max = 500, message = "Comment cannot exceed 500 characters.")
    private String comment;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
