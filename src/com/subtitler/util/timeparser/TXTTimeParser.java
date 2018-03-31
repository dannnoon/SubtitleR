package com.subtitler.util.timeparser;

import com.subtitler.data.IllegalTimeFormatException;
import com.subtitler.data.Time;
import com.subtitler.data.TimePatterns;

public class TXTTimeParser implements TimeParser {

    @Override
    public Time[] parseStringLine(String line) throws IllegalTimeFormatException {
        if (!line.matches(TimePatterns.TXT_LINE_PATTERN)) {
            throw new IllegalTimeFormatException();
        }

        int firstBracketIndex = line.indexOf("}");
        int secondBracketIndex = line.indexOf("}", firstBracketIndex + 1);

        String firstTimeString = line.substring(0, firstBracketIndex + 1);
        String secondTimeString = line.substring(firstBracketIndex + 1, secondBracketIndex + 1);

        return new Time[]{
                parseString(firstTimeString),
                parseString(secondTimeString)
        };
    }

    @Override
    public Time parseString(String time) throws IllegalTimeFormatException {
        if (!time.matches(TimePatterns.TXT_TIME_PATTERN)) {
            throw new IllegalTimeFormatException();
        }

        int milliseconds = Integer.parseInt(time.substring(1, time.length() - 1));
        int seconds = milliseconds / 1000;
        milliseconds = milliseconds % 1000;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        int hours = minutes / 60;
        minutes = minutes % 60;

        Time parsedTime = new Time();

        parsedTime.setHour(hours);
        parsedTime.setMinute(minutes);
        parsedTime.setSecond(seconds);
        parsedTime.setMillisecond(milliseconds);

        return parsedTime;
    }

    @Override
    public String modifyTimeLine(String line, Time timeDifference) throws IllegalTimeFormatException {
        Time[] times = parseStringLine(line);

        String[] splittedLine = line.split("}");
        StringBuilder data = new StringBuilder();
        for (int i = 2; i < splittedLine.length; i++) {
            data
                    .append(splittedLine[i])
                    .append("}");
        }

        times[0] = times[0].add(timeDifference);
        times[1] = times[1].add(timeDifference);

        return String.format("%s%s%s\n", parseTime(times[0]), parseTime(times[1]), data.toString());
    }

    @Override
    public String parseTime(Time time) {
        int millisFromHour = time.getHour() * 60 * 60 * 1000;
        int millisFromMinute = time.getMinute() * 60 * 1000;
        int millisFromSecond = time.getSecond() * 1000;
        int milliseconds = millisFromHour + millisFromMinute + millisFromSecond + time.getMillisecond();

        return String.format("{%d}", milliseconds);
    }
}
