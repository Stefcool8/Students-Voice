package org.example.studentsvoice.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.studentsvoice.entity.TimeRange;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class TimeRangeRepository implements BaseRepository<TimeRange, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public TimeRange save(TimeRange evaluationTimeRange) {
        if (evaluationTimeRange.getId() == null) {
            entityManager.persist(evaluationTimeRange);
            return evaluationTimeRange;
        }
        return entityManager.merge(evaluationTimeRange);
    }

    @Override
    public Optional<TimeRange> findById(Long id) {
        return Optional.ofNullable(entityManager.find(TimeRange.class, id));
    }

    @Override
    public List<TimeRange> findAll() {
        return entityManager.createNamedQuery("TimeRange.findAll", TimeRange.class).getResultList();
    }

    @Override
    public Optional<TimeRange> update(TimeRange evaluationTimeRange) {
        return Optional.ofNullable(entityManager.merge(evaluationTimeRange));
    }

    @Override
    public void deleteById(Long id) {
        TimeRange evaluationTimeRange = entityManager.find(TimeRange.class, id);
        if (evaluationTimeRange != null) {
            entityManager.remove(evaluationTimeRange);
        }
    }
}
