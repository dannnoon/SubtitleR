package com.subtitler.data;

public class TimePatterns {

    public static final String SRT_TIME_PATTERN = "\\d\\d:\\d\\d:\\d\\d,\\d\\d\\d";
    public static final String SRT_LINE_PATTERN = String.format("%s --> %s", SRT_TIME_PATTERN, SRT_TIME_PATTERN);

    public static final String TXT_TIME_PATTERN = "\\{[0-9]+\\}";
    public static final String TXT_LINE_PATTERN = String.format("%s%s.*", TXT_TIME_PATTERN, TXT_TIME_PATTERN);
}
