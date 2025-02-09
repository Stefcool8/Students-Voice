package org.example.studentsvoice.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.example.studentsvoice.dto.Evaluation.EvaluationAdminDTO;
import org.example.studentsvoice.dto.Evaluation.EvaluationStudentRequestDTO;
import org.example.studentsvoice.dto.Evaluation.EvaluationStudentResponseDTO;
import org.example.studentsvoice.dto.Evaluation.EvaluationTeacherDTO;
import org.example.studentsvoice.entity.Evaluation;
import org.example.studentsvoice.entity.User;
import org.example.studentsvoice.service.UserService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EvaluationMapper {
    @Inject
    private UserService userService;

    public EvaluationAdminDTO toAdminDTO(Evaluation evaluation) {
        return EvaluationAdminDTO.builder()
                .id(evaluation.getId())
                .registrationNumber(evaluation.getRegistrationNumber().toString())
                .teacherName(evaluation.getTeacher().getName())
                .studentName(evaluation.getStudent().getName())
                .activityName(evaluation.getActivityName())
                .activityType(evaluation.getActivityType())
                .grade(evaluation.getGrade())
                .comment(evaluation.getComment())
                .timestamp(evaluation.getTimestamp().toString())
                .build();
    }

    public EvaluationStudentResponseDTO toStudentResponseDTO(Evaluation evaluation) {
        return EvaluationStudentResponseDTO.builder()
                .id(evaluation.getId())
                .teacherName(evaluation.getTeacher().getName())
                .activityName(evaluation.getActivityName())
                .activityType(evaluation.getActivityType())
                .grade(evaluation.getGrade())
                .comment(evaluation.getComment())
                .timestamp(evaluation.getTimestamp().toString())
                .build();
    }

    public Evaluation studentRequestDTOtoEntity(EvaluationStudentRequestDTO evaluationStudentRequestDTO, User student) {
        Optional<User> teacher = userService.findByName(evaluationStudentRequestDTO.getTeacherName());
        return new Evaluation(
                null,
                null,
                teacher.orElse(null),
                student,
                evaluationStudentRequestDTO.getActivityName(),
                evaluationStudentRequestDTO.getActivityType(),
                evaluationStudentRequestDTO.getGrade(),
                evaluationStudentRequestDTO.getComment(),
                null
        );
    }

    public EvaluationTeacherDTO toTeacherDTO(Evaluation evaluation) {
        return EvaluationTeacherDTO.builder()
                .id(evaluation.getId())
                .activityName(evaluation.getActivityName())
                .activityType(evaluation.getActivityType())
                .grade(evaluation.getGrade())
                .comment(evaluation.getComment())
                .timestamp(evaluation.getTimestamp().toString())
                .build();
    }

    public List<EvaluationAdminDTO> toAdminDTOs(List<Evaluation> evaluations) {
        return evaluations.stream().map(this::toAdminDTO).toList();
    }

    public List<EvaluationTeacherDTO> toTeacherDTOs(List<Evaluation> evaluations) {
        return evaluations.stream().map(this::toTeacherDTO).toList();
    }

    public List<EvaluationStudentResponseDTO> toStudentResponseDTOs(List<Evaluation> evaluations) {
        return evaluations.stream().map(this::toStudentResponseDTO).toList();
    }
}

