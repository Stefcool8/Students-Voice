package com.example.studentsvoice.core.dto.TimeRange;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeRangeRequestDTO {
    @NotNull(message = "Start time cannot be null.")
    private String startTime;

    @NotNull(message = "End time cannot be null.")
    private String endTime;
}
