package com.subtitler.util.subtitlemanager;

import com.subtitler.data.IllegalTimeFormatException;
import com.subtitler.data.Time;
import com.subtitler.util.timeparser.TimeParser;
import com.sun.istack.internal.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class SubtitleManager {

    private static final String TMP_FILE_NAME = "subtitleR.tmp";

    private final File file;
    private final Time originalStartTime;
    private final TimeParser timeParser;
    private Time startTime;

    SubtitleManager(File file, TimeParser timeParser) {
        this.file = file;
        this.timeParser = timeParser;
        originalStartTime = extractStartTime();
        startTime = originalStartTime;
    }

    @Nullable
    private Time extractStartTime() {
        String line;

        try (BufferedReader reader = openReader()) {
            while ((line = reader.readLine()) != null) {
                try {
                    Time[] times = timeParser.parseStringLine(line);
                    return times[0];
                } catch (IllegalTimeFormatException ignored) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void changeStartTime(Time time) {
        Time timeDifference = time.difference(startTime);

        File tmpFile = new File(file.getParentFile(), TMP_FILE_NAME);
        try {
            if (tmpFile.exists() || tmpFile.createNewFile()) {
                try (BufferedReader reader = openReader()) {
                    try (OutputStreamWriter writer = openWriter(tmpFile)) {
                        String line;

                        while ((line = reader.readLine()) != null) {
                            try {
                                writer.write(timeParser.modifyTimeLine(line, timeDifference));
                            } catch (IllegalTimeFormatException error) {
                                writer.write(String.format("%s\n", line));
                            }
                            writer.flush();
                        }

                        writer.close();
                        reader.close();
                        Files.move(tmpFile.toPath(), file.toPath(), REPLACE_EXISTING);
                        startTime = extractStartTime();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedReader openReader() throws FileNotFoundException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.ISO_8859_1));
    }

    private OutputStreamWriter openWriter(File file) throws FileNotFoundException {
        return new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.ISO_8859_1);
    }

    public File getFile() {
        return file;
    }

    public Time getOriginalStartTime() {
        return originalStartTime;
    }

    public Time getStartTime() {
        return startTime;
    }
}
