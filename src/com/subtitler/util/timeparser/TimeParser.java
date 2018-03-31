package com.subtitler.util.timeparser;

import com.subtitler.data.IllegalTimeFormatException;
import com.subtitler.data.Time;

public interface TimeParser {

    Time[] parseStringLine(String line) throws IllegalTimeFormatException;

    Time parseString(String time) throws IllegalTimeFormatException;

    String modifyTimeLine(String line, Time timeDifference) throws IllegalTimeFormatException;

    String parseTime(Time time);
}
