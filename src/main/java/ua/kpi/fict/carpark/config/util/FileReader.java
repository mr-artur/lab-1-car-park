package ua.kpi.fict.carpark.config.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class FileReader {

    public static final String FILENAME_IS_NULL = "fileName should not be null";
    public static final String INPUT_STREAM_IS_NULL = "File '%s' was not found";

    public List<String> readFile(String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException(FILENAME_IS_NULL);
        }
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException(String.format(INPUT_STREAM_IS_NULL, fileName));
        }
        return new BufferedReader(new InputStreamReader(inputStream))
            .lines()
            .parallel()
            .collect(Collectors.toList());
    }

    public List<String> readFromLine(String fileName, int fromLine) {
        List<String> allLines = readFile(fileName);
        return allLines.subList(fromLine - 1, allLines.size());
    }

    public List<String> readWithoutHeader(String fileName) {
        final int firstLineWithData = 2;

        return readFromLine(fileName, firstLineWithData);
    }
}
