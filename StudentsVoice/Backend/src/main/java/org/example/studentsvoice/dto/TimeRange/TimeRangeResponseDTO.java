package org.example.studentsvoice.dto.TimeRange;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeRangeResponseDTO {
    private String startTime;
    private String endTime;
}
