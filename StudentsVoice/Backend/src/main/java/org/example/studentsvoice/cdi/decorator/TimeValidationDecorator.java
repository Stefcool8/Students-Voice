package org.example.studentsvoice.cdi.decorator;

import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;
import org.example.studentsvoice.entity.Evaluation;
import org.example.studentsvoice.service.BaseService;
import org.example.studentsvoice.service.TimeRangeService;

import java.time.LocalDateTime;

@Decorator
public abstract class TimeValidationDecorator implements BaseService<Evaluation, Long> {

    @Inject
    @Delegate
    private BaseService<Evaluation, Long> delegate;

    @Inject
    private TimeRangeService timeRangeService;

    @Override
    public Evaluation save(Evaluation evaluation, String... params) {
        LocalDateTime now = LocalDateTime.now();
        if (!timeRangeService.isWithinRange(now)) {
            throw new IllegalStateException("Operation not allowed: outside of the permitted time range.");
        }
        // Delegate the actual save operation to the wrapped service
        return delegate.save(evaluation);
    }
}
