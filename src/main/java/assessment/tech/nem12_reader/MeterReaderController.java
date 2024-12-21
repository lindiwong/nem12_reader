package assessment.tech.nem12_reader;

import assessment.tech.nem12_reader.services.MeterProcessingService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/readings")
public class MeterReaderController {
    private final MeterProcessingService service;

    public MeterReaderController(MeterProcessingService service) {
        this.service = service;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> consume(@RequestParam("file") MultipartFile file) {
        return service.process(file);
    }
}
