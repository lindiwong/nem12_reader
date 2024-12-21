package assessment.tech.nem12_reader.utilities;

import assessment.tech.nem12_reader.exceptions.CsvProcessingException;
import assessment.tech.nem12_reader.exceptions.InvalidCsvFormatException;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public final class CsvUtility {
    public static void processFile(MultipartFile file, Consumer<String[]> consumer) throws CsvProcessingException {
        if (file == null || file.isEmpty()) {
            throw new InvalidCsvFormatException("File is empty or null");
        }

        if (file.getContentType() == null || !file.getContentType().equalsIgnoreCase("text/csv")) {
            throw new InvalidCsvFormatException("File is not in CSV format");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            reader.lines().map(line -> line.split(",")).forEach(consumer);
        } catch (IOException ex) {
            throw new CsvProcessingException("Failed to process csv file: %s".formatted(ex.getMessage()));
        }
    }
}
