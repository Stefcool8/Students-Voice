package org.example.studentsvoice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "time_range")
@NamedQueries({
        @NamedQuery(name = "TimeRange.findAll", query = "SELECT e FROM TimeRange e")
})
@ToString
public class TimeRange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time", nullable = false)
    @NotNull(message = "Start time cannot be null.")
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    @NotNull(message = "End time cannot be null.")
    @Future(message = "End time must be in the future.")
    private LocalDateTime endTime;
}
