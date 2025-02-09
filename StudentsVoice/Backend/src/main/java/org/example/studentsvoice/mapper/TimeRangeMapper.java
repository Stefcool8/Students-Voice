package org.example.studentsvoice.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import org.example.studentsvoice.dto.TimeRange.TimeRangeResponseDTO;
import org.example.studentsvoice.dto.TimeRange.TimeRangeRequestDTO;
import org.example.studentsvoice.entity.TimeRange;

import java.time.LocalDateTime;

@ApplicationScoped
public class TimeRangeMapper {
    public TimeRangeResponseDTO toDTO(TimeRange entity) {
        return TimeRangeResponseDTO.builder()
                .startTime(entity.getStartTime().toString())
                .endTime(entity.getEndTime().toString())
                .build();
    }

    public TimeRange toEntity(TimeRangeRequestDTO dto) {
        return new TimeRange(
                null,
                LocalDateTime.parse(dto.getStartTime()),
                LocalDateTime.parse(dto.getEndTime())
        );
    }
}
