package com.example.studentsvoice.evaluationservice.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import com.example.studentsvoice.evaluationservice.cdi.interceptor.Loggable;
import com.example.studentsvoice.evaluationservice.cdi.producer.MyLogger;
import com.example.studentsvoice.evaluationservice.cdi.producer.RegistrationNumber;
import com.example.studentsvoice.evaluationservice.entity.Evaluation;
import com.example.studentsvoice.evaluationservice.repository.EvaluationRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Loggable
@ApplicationScoped
public class EvaluationService implements BaseService<Evaluation, Long> {
    @Inject
    @MyLogger
    private Logger LOGGER;

    @Inject
    private EvaluationRepository evaluationRepository;

    @Inject
    @RegistrationNumber
    private Instance<UUID> registrationNumberGenerator;

    @Override
    @Transactional
    public Evaluation save(Evaluation evaluation, String... params) {
        if (evaluationRepository.findByStudentTeacherActivity(evaluation).isEmpty()) {
            evaluation.setRegistrationNumber(registrationNumberGenerator.get());
            Evaluation savedEvaluation = evaluationRepository.save(evaluation);

            return savedEvaluation;
        } else {
            throw new IllegalArgumentException("An evaluation for this student, teacher and activity already exists.");
        }
    }

    @Override
    public Optional<Evaluation> findById(Long id) {
        LOGGER.info("Fetching evaluation from database");
        return evaluationRepository.findById(id);
    }

    @Override
    public List<Evaluation> findAll() {
        LOGGER.info("Fetching evaluations from database");
        return evaluationRepository.findAll();
    }

    @Override
    public Optional<Evaluation> update(Evaluation evaluation) {
        return evaluationRepository.update(evaluation);
    }

    @Override
    public void deleteById(Long id) {
        evaluationRepository.deleteById(id);
    }

    public List<Evaluation> findByTeacherId(Long teacherId) {
        LOGGER.info("Fetching evaluations by teacher from database");
        return evaluationRepository.findByTeacherId(teacherId);
    }

    public List<Evaluation> findByStudentId(Long studentId) {
        return evaluationRepository.findByStudentId(studentId);
    }

    public long countEvaluations() {
        return evaluationRepository.findAll().size();
    }

    public double averageGrade() {
        return evaluationRepository.findAll().stream()
                .mapToInt(Evaluation::getGrade)
                .average()
                .orElse(0.0);
    }

    public Map<String, Double> averageGradePerTeacher() {
        return evaluationRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        e -> e.getTeacher().getName(),
                        Collectors.averagingInt(Evaluation::getGrade)
                ));
    }
}
