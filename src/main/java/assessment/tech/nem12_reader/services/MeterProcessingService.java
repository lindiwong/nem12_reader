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
import java.util.List;

@Service
public class MeterProcessingService {
    private static final Logger log = LoggerFactory.getLogger(MeterProcessingService.class);
    private final MeterReadingRepository repository;
    private final Nem12Utility nem12Utility;

    public MeterProcessingService(MeterReadingRepository repository) {
        this.repository = repository;
        this.nem12Utility = new Nem12Utility();
    }

    public void process(MultipartFile file) throws CsvProcessingException, InvalidCsvFormatException {
        List<String[]> records = CsvUtility.processFile(file);
        List<MeterReading> readings = nem12Utility.process(records);

        if (!readings.isEmpty()) {
            // todo: we could implement retry with exponential backoff but
            //  i dont see a reason to implement it here right now :)
            repository.saveAll(readings);
        }

        log.info("Successfully processed %s records".formatted(readings.size()));
    }
}
