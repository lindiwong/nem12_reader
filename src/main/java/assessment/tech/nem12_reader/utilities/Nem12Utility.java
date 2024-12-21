package assessment.tech.nem12_reader.utilities;

import assessment.tech.nem12_reader.exceptions.InvalidNem12FormatException;
import assessment.tech.nem12_reader.models.MeterReading;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Nem12Utility {

    @Getter
    public static class NmiData {
        String nmi;
        int intervalLength;
    }

    private static final String RECORD_TYPE_HEADER = "100";
    private static final String RECORD_TYPE_NMI_DETAIL = "200";
    private static final String RECORD_TYPE_INTERVAL = "300";
    private static final String RECORD_TYPE_B2B = "500";
    private static final String RECORD_TYPE_EOD = "900";

    public List<MeterReading> process(List<String[]> records) {
        if (records == null || records.isEmpty()) {
            throw new InvalidNem12FormatException("No records found.");
        }

        boolean hasEndRecord = false;
        NmiData currentNmiData = null;
        List<MeterReading> results = new ArrayList<>();

        for (String[] record : records) {
            if (record.length == 0) {
                continue;
            }

            String recordType = record[0].trim();
            switch (recordType) {
                case RECORD_TYPE_HEADER:
                    validateHeaderRecord(record);
                    break;
                case RECORD_TYPE_NMI_DETAIL:
                    currentNmiData = processNmiDetailDataRecord(record);
                    break;
                case RECORD_TYPE_INTERVAL:
                    results.addAll(processIntervalDataRecord(record, currentNmiData));
                    break;
                case RECORD_TYPE_B2B:
                    break;
                case RECORD_TYPE_EOD:
                    currentNmiData = null;
                    hasEndRecord = true;
                    break;
                default:
                    throw new InvalidNem12FormatException("Unknown record type %s".formatted(recordType));
            }
        }

        if (!hasEndRecord) {
            throw new InvalidNem12FormatException("No end (900) record found");
        }

        return results;
    }

    /**
     * To validate header record (100) has 5 columns:
     * <RecordIndicator>,<VersionHeader>,<DateTime>,<FromParticipant>,<ToParticipant>
     */
    private void validateHeaderRecord(String[] records) {
        if (records.length < 5) {
            throw new InvalidNem12FormatException("Invalid header record format");
        }
    }

    private NmiData processNmiDetailDataRecord(String[] records) {
        NmiData data = new NmiData();
        data.nmi = records[4].trim();
        data.intervalLength = Integer.parseInt(records[8].trim());
        return data;
    }

    private List<MeterReading> processIntervalDataRecord(String[] record, NmiData currentNmiData) {
        if (currentNmiData == null) {
            throw new InvalidNem12FormatException("Interval data found before processing NMI data details.");
        }

        int intervalIndex = 2;
        int intervalsPerDay = 1440 / currentNmiData.intervalLength;

        List<MeterReading> readings = new ArrayList<>();
        if (record.length < (intervalIndex + intervalsPerDay)) {
            throw new InvalidNem12FormatException("Not enough intervals within NMI %s.".formatted(currentNmiData.nmi));
        }

        for (int i = 2; i < intervalsPerDay; i++) {
            String value = record[i].trim();

            BigDecimal consumption;
            try {
                consumption = new BigDecimal(value);
            } catch (NumberFormatException ex) {
                throw new InvalidNem12FormatException("Invalid interval consumption format");
            }

            String dateRaw = record[1].trim();
            LocalDate date = LocalDate.parse(
                    dateRaw.substring(0,4) + "-" + dateRaw.substring(4,6) + "-" + dateRaw.substring(6,8));
            LocalDateTime timestamp = date.atTime(LocalTime.MIDNIGHT).plusMinutes((long) i * currentNmiData.intervalLength);
            readings.add(new MeterReading(
                    currentNmiData.nmi, timestamp, consumption
            ));
        }

        return readings;
    }
}
