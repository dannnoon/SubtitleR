package com.subtitler.util.timeparser;

import com.subtitler.data.IllegalTimeFormatException;
import com.subtitler.data.Time;
import com.subtitler.data.TimePatterns;

public class SRTTimeParser implements TimeParser {

    @Override
    public Time[] parseStringLine(String line) throws IllegalTimeFormatException {
        if (!line.matches(TimePatterns.SRT_LINE_PATTERN)) {
            throw new IllegalTimeFormatException();
        }

        String[] timeStrings = line.split(" --> ");

        return new Time[]{
                parseString(timeStrings[0]),
                parseString(timeStrings[1])
        };
    }

    @Override
    public Time parseString(String time) throws IllegalTimeFormatException {
        if (!time.matches(TimePatterns.SRT_TIME_PATTERN)) {
            throw new IllegalTimeFormatException();
        }

        int hour = Integer.parseInt(time.substring(0, 2));
        int minute = Integer.parseInt(time.substring(3, 5));
        int second = Integer.parseInt(time.substring(6, 8));
        int millisecond = Integer.parseInt(time.substring(9, 12));

        Time parsedTime = new Time();

        parsedTime.setHour(hour);
        parsedTime.setMinute(minute);
        parsedTime.setSecond(second);
        parsedTime.setMillisecond(millisecond);

        return parsedTime;
    }

    @Override
    public String modifyTimeLine(String line, Time timeDifference) throws IllegalTimeFormatException {
        Time[] times = parseStringLine(line);

        times[0] = times[0].add(timeDifference);
        times[1] = times[1].add(timeDifference);

        return String.format("%s --> %s\n", parseTime(times[0]), parseTime(times[1]));
    }

    @Override
    public String parseTime(Time time) {
        return String.format("%02d:%02d:%02d,%03d", time.getHour(), time.getMinute(), time.getSecond(), time.getMillisecond());
    }
}
