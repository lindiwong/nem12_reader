package assessment.tech.nem12_reader.utilities;

import assessment.tech.nem12_reader.exceptions.InvalidNem12FormatException;
import assessment.tech.nem12_reader.models.MeterReading;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Nem12Utility {

    public static class NmiData {
        String nmi;
        int intervalLength;
        LocalDate timestamp;
    }

    private NmiData currentNmi = null;
    private boolean headerProcessed = false;

    @Getter private final List<MeterReading> readings = new ArrayList<>();


    public static List<MeterReading> process(String[] readings) {
        return new ArrayList<>();
    }
    /**
     * To validate header record (100) is in accordance to format:
     * <RecordIndicator>,<VersionHeader>,<DateTime>,<FromParticipant>,<ToParticipant>
     */
    public void processHeaderRecord() {
        if (headerProcessed) {
            throw new InvalidNem12FormatException("Duplicate header record found.");
        }

//        if (!readings.isEmpty() || currentNmi != null) {
//            throw new InvalidNem12FormatException("NEM12 data is not in order. Data does not start with header 100 record.");
//        }

        headerProcessed = true;
    }

    /**
     * To validate NMI data details record (200) is in accordance to format:
     * <RecordIndicator>,<NMI>,<NMIConfiguration>,<RegisterID>,<NMISuffix>,<MDMDataStreamIdentifier>,
     * <MeterSerialNumber>,<UOM>,<IntervalLength>,<NextScheduledReadDate>
     */
    public void isValidNmiDataDetailsRecord() {
        if (!headerProcessed) {
            throw new InvalidNem12FormatException("NMI data details found before header record.");
        }
    }

    /**
     * To validate interval data record (300) is in accordance to format:
     * RecordIndicator,IntervalDate,IntervalValue1 . . . IntervalValueN,
     * QualityMethod,ReasonCode,ReasonDescription,UpdateDateTime,MSATSLoadDateTime
     */
    public void isValidIntervalDataRecord(String[] readings) {
        if (currentNmi == null) {
            throw new InvalidNem12FormatException("Interval data found before processing NMI data details.");
        }
    }
}
