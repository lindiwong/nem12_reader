package assessment.tech.nem12_reader.utilities;

import assessment.tech.nem12_reader.exceptions.InvalidNem12FormatException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;


public class Nem12UtilityTest {
    private final Nem12Utility utility = new Nem12Utility();

    @Test
    void test_invalid_header_columns() {
        List<String[]> records = List.of(
                new String[]{"100", "NEM12"},
                new String[]{"200", "NCDE001111", "E1B1Q1E2", "1", "E1", "N1", "METSER123", "Wh", "15"}
        );

        InvalidNem12FormatException exception = Assertions.assertThrows(
                InvalidNem12FormatException.class,
                () -> utility.process(records)
        );

        Assertions.assertEquals("Invalid header record format.", exception.getMessage());
    }

    @Test
    void test_insufficient_consumption_value() {
        List<String[]> records = List.of(
                new String[]{"100", "NEM12", "202312070911", "MDA1", "Ret1"},
                new String[]{"200", "NCDE001111", "E1B1Q1E2", "1", "E1", "N1", "METSER123", "Wh", "15"},
                new String[]{"300", "20031204","10","10","10","A","","","20031206011132","20031207011022"},
                new String[]{"900"}
        );

        InvalidNem12FormatException exception = Assertions.assertThrows(
                InvalidNem12FormatException.class,
                () -> utility.process(records)
        );

        Assertions.assertEquals("Not enough intervals within NMI E1.", exception.getMessage());
    }

    @Test
    void test_invalid_consumption_value() {
        List<String[]> records = List.of(
                new String[]{"100", "NEM12", "202312070911", "MDA1", "Ret1"},
                new String[]{"200", "NCDE001111", "E1B1Q1E2", "1", "E1", "N1", "METSER123", "Wh", "15"},
                new String[]{"300", "20031204","0","0","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","invalid","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","A","","","20031206011132","20031207011022"},
                new String[]{"900"}
        );

        InvalidNem12FormatException exception = Assertions.assertThrows(
                InvalidNem12FormatException.class,
                () -> utility.process(records)
        );

        Assertions.assertEquals("Invalid interval consumption format", exception.getMessage());
    }

    @Test
    void test_empty_nem12file() {
        List<String[]> records = Collections.emptyList();
        Assertions.assertThrows(InvalidNem12FormatException.class, () -> utility.process(records));
    }

    @Test
    void test_unknown_recordType() {
        List<String[]> records = List.of(
                new String[]{"700", "weird", "value"},
                new String[]{"710", "that", "shouldnt-be", "here"}
        );
        Assertions.assertThrows(InvalidNem12FormatException.class, () -> utility.process(records));
    }

    @Test
    void test_missing_footer() {
        List<String[]> records = List.of(
                new String[]{"100", "NEM12", "202312070911", "MDA1", "Ret1"},
                new String[]{"200", "NCDE001111", "E1B1Q1E2", "1", "E1", "N1", "METSER123", "Wh", "15"},
                new String[]{"300", "20031204","0","0","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","8.773","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","A","","","20031206011132","20031207011022"}
            );
        InvalidNem12FormatException exception = Assertions.assertThrows(InvalidNem12FormatException.class,
                () -> utility.process(records));

        Assertions.assertEquals("No end record found (record 900)", exception.getMessage());
    }
}
