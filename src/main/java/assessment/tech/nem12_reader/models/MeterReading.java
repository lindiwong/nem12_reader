package assessment.tech.nem12_reader.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Getter
@ToString
@Entity
@Table(name = "meter_readings", uniqueConstraints = @UniqueConstraint(columnNames = {"nmi", "timestamp"}))
public class MeterReading {
    @Id
    @GeneratedValue()
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(length = 10, nullable = false)
    private String nmi;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false, precision = 15, scale = 6)
    private BigDecimal consumption;

    public MeterReading(String nmi, LocalDateTime timestamp, BigDecimal consumption) {
        this.nmi = nmi;
        this.timestamp = timestamp;
        this.consumption = consumption;
    }
}