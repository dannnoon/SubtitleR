package com.subtitler.util.subtitlemanager;

import com.sun.istack.internal.NotNull;

import java.io.File;

public class SubtitleManagerFactory {

    private static final String SRT_EXTENSION = "srt";
    private static final String TXT_EXTENSION = "txt";

    public static SubtitleManager create(@NotNull File file) {
        String extension = getExtension(file);

        switch (extension) {
            case SRT_EXTENSION: {
                return new SRTSubtitleManager(file);
            }

            case TXT_EXTENSION: {
                return new TXTSubtitleParser(file);
            }

            default: {
                throw new IllegalStateException(String.format("Provided file type is not supported (%s).", extension));
            }
        }
    }

    private static String getExtension(File file) {
        int pos = file.getName().lastIndexOf(".");

        if (pos > 0) {
            return file.getName().substring(pos + 1);
        } else {
            return "";
        }
    }
}
