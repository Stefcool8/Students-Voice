package org.example.studentsvoice.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.example.studentsvoice.cdi.interceptor.Loggable;
import org.example.studentsvoice.entity.TimeRange;
import org.example.studentsvoice.repository.TimeRangeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Loggable
@ApplicationScoped
public class TimeRangeService implements BaseService<TimeRange, Long> {

    @Inject
    private TimeRangeRepository timeRangeRepository;

    @Override
    public TimeRange save(TimeRange timeRange, String... params) {
        return timeRangeRepository.save(timeRange);
    }

    @Override
    public Optional<TimeRange> findById(Long id) {
        return timeRangeRepository.findById(id);
    }

    @Override
    public List<TimeRange> findAll() {
        return timeRangeRepository.findAll();
    }

    @Override
    public Optional<TimeRange> update(TimeRange timeRange) {
        return timeRangeRepository.update(timeRange);
    }

    @Override
    public void deleteById(Long id) {
        timeRangeRepository.deleteById(id);
    }

    public boolean isWithinRange(LocalDateTime time) {
        Long rangeId = 1L;
        return timeRangeRepository.findById(rangeId)
                .map(range -> range.getStartTime().isBefore(time) && range.getEndTime().isAfter(time))
                .orElse(false);
    }
}
