package assessment.tech.nem12_reader.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CsvProcessingException.class)
    public ResponseEntity<Map<String, String>> handleCsvProcessingException(CsvProcessingException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Error processing CSV");
        response.put("details", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCsvFormatException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCsvFormat(InvalidCsvFormatException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid CSV format");
        response.put("details", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidNem12FormatException.class)
    public ResponseEntity<Map<String, String>> handleInvalidNem12Format(InvalidNem12FormatException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid NEM12 format");
        response.put("details", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Internal server error");
        response.put("details", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
