package assessment.tech.nem12_reader.exceptions;

import java.io.IOException;

public class CsvProcessingException extends IOException {
    public CsvProcessingException(String message) {
        super(message);
    }
}
