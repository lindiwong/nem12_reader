package assessment.tech.nem12_reader.services;

import assessment.tech.nem12_reader.exceptions.CsvProcessingException;
import assessment.tech.nem12_reader.exceptions.InvalidCsvFormatException;
import assessment.tech.nem12_reader.models.MeterReading;
import assessment.tech.nem12_reader.repositories.MeterReadingRepository;
import assessment.tech.nem12_reader.utilities.CsvUtility;
import assessment.tech.nem12_reader.utilities.Nem12Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MeterProcessingService {
    private static final Logger log = LoggerFactory.getLogger(MeterProcessingService.class);
    private final MeterReadingRepository repository;


    public MeterProcessingService(MeterReadingRepository repository) {
        this.repository = repository;
    }

    public void process(MultipartFile file) throws CsvProcessingException, InvalidCsvFormatException {
        final List<MeterReading> newReadings = new ArrayList<>();
        final int[] count = {0};

        CsvUtility.processFile(file, record -> {
            List<MeterReading> readings = Nem12Utility.process(record);
            if (!readings.isEmpty()) {
                newReadings.addAll(readings);
                count[0] += readings.size();
            }
        });

        if (!newReadings.isEmpty()) {
            repository.saveAll(newReadings);
        }

        log.info("Successfully processed %s records.".formatted(count[0]));
    }
}
