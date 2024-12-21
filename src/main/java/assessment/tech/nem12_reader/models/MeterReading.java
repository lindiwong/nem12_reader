package assessment.tech.nem12_reader.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class MeterReading {
    private UUID id;
    @Setter private String nmi;
    @Setter private LocalDateTime timestamp;
    @Setter private BigDecimal consumption;
}