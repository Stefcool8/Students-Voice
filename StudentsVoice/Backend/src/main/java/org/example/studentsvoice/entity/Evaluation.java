package org.example.studentsvoice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "evaluations")
@NamedQueries({
        @NamedQuery(name = "Evaluation.findAll", query = "SELECT e FROM Evaluation e"),
        @NamedQuery(name = "Evaluation.findByTeacherId", query = "SELECT e FROM Evaluation e WHERE e.teacher.id = :teacherId"),
        @NamedQuery(name = "Evaluation.findByStudentId", query = "SELECT e FROM Evaluation e WHERE e.student.id = :studentId"),
        @NamedQuery(name = "Evaluation.findByStudentTeacherActivity",
                query = "SELECT e FROM Evaluation e WHERE e.student.id = :studentId AND e.teacher.id = :teacherId " +
                        "AND e.activityName = :activity_name AND e.activityType = :activity_type")
})
@ToString
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "registration_number", nullable = false, unique = true, updatable = false)
    private UUID registrationNumber;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    @NotNull(message = "Teacher is required.")
    private User teacher;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    @NotNull(message = "Student is required.")
    private User student;

    @Column(name = "activity_name", nullable = false)
    @NotBlank(message = "Activity name cannot be blank.")
    private String activityName;

    @Column(name = "activity_type", nullable = false)
    @NotBlank(message = "Activity type cannot be blank.")
    private String activityType;

    @Column(nullable = false)
    @Min(value = 1, message = "Grade must be at least 1.")
    @Max(value = 10, message = "Grade cannot be greater than 10.")
    private Integer grade;

    @Size(max = 500, message = "Comment cannot exceed 500 characters.")
    private String comment;

    @CreationTimestamp
    private LocalDateTime timestamp;
}
