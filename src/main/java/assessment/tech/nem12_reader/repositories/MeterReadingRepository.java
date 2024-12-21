package assessment.tech.nem12_reader.repositories;

import assessment.tech.nem12_reader.models.MeterReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MeterReadingRepository extends JpaRepository<MeterReading, UUID> {}
