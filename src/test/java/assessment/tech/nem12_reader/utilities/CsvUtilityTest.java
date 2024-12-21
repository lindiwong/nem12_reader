package assessment.tech.nem12_reader.utilities;

import assessment.tech.nem12_reader.exceptions.CsvProcessingException;
import assessment.tech.nem12_reader.exceptions.InvalidCsvFormatException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.InputStream;
import java.util.List;

public class CsvUtilityTest {
    @Test
    void test_valid_file() throws Exception {
        int interval = 15;
        String data = "100,NEM12,200402070911,MDA1,Ret1\n" +
                "       200,NCDE001111,E1B1Q1E2,1,E1,N1,METSER123,Wh,%s,\n".formatted(interval) +
                "       300,20031204,0,0,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,A,,,20031206011132,20031207011022\n" +
                "       900";

        MockMultipartFile file = new MockMultipartFile("nem12file", "nem12file_sample.csv", "text/csv", data.getBytes());
        List<String[]> result = CsvUtility.processFile(file);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(4, result.size());

        Assertions.assertArrayEquals(new String[]{
                "100", "NEM12", "200402070911", "MDA1", "Ret1"
        }, result.getFirst());
        Assertions.assertArrayEquals(new String[]{
                "200", "NCDE001111", "E1B1Q1E2", "1", "E1", "N1", "METSER123", "Wh", "15"
        }, result.get(1));
        Assertions.assertTrue(result.get(2).length > (24 * 60) / interval + 2);
    }

    @Test
    void test_empty_file() throws Exception {
        MockMultipartFile file = new MockMultipartFile("nem12file", "nem12file_sample.csv", "text/csv", new byte[0]);
        Assertions.assertThrows(InvalidCsvFormatException.class, () -> CsvUtility.processFile(file));
    }

    @Test
    void test_corrupted_file() throws Exception {
        // just testing for IOException so set input stream as null
        MockMultipartFile file = new MockMultipartFile("nem12file", "nem12file_sample.csv", "text/csv", InputStream.nullInputStream());
        Assertions.assertThrows(CsvProcessingException.class, () -> CsvUtility.processFile(file));
    }
}
