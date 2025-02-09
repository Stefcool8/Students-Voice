package org.example.studentsvoice.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.studentsvoice.entity.Evaluation;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class EvaluationRepository implements BaseRepository<Evaluation, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Evaluation save(Evaluation evaluation) {
        if (evaluation.getId() == null) {
            entityManager.persist(evaluation);
            return evaluation;
        }
        return entityManager.merge(evaluation);
    }

    @Override
    public Optional<Evaluation> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Evaluation.class, id));
    }

    @Override
    public List<Evaluation> findAll() {
        return entityManager.createNamedQuery("Evaluation.findAll", Evaluation.class).getResultList();
    }

    @Override
    public Optional<Evaluation> update(Evaluation evaluation) {
        return Optional.ofNullable(entityManager.merge(evaluation));
    }

    @Override
    public void deleteById(Long id) {
        Evaluation evaluation = entityManager.find(Evaluation.class, id);
        if (evaluation != null) {
            entityManager.remove(evaluation);
        }
    }

    public List<Evaluation> findByTeacherId(Long teacherId) {
        return entityManager.createNamedQuery("Evaluation.findByTeacherId", Evaluation.class)
                .setParameter("teacherId", teacherId)
                .getResultList();
    }

    public List<Evaluation> findByStudentId(Long studentId) {
        return entityManager.createNamedQuery("Evaluation.findByStudentId", Evaluation.class)
                .setParameter("studentId", studentId)
                .getResultList();
    }

    public Optional<Evaluation> findByStudentTeacherActivity(Evaluation evaluation) {
        List<Evaluation> evaluations = entityManager.createNamedQuery("Evaluation.findByStudentTeacherActivity", Evaluation.class)
                .setParameter("studentId", evaluation.getStudent().getId())
                .setParameter("teacherId", evaluation.getTeacher().getId())
                .setParameter("activity_name", evaluation.getActivityName())
                .setParameter("activity_type", evaluation.getActivityType())
                .getResultList();
        return evaluations.isEmpty() ? Optional.empty() : Optional.of(evaluations.getFirst());
    }
}
