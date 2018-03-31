package com.subtitler.util.subtitlemanager;

import com.subtitler.util.timeparser.SRTTimeParser;

import java.io.File;

class SRTSubtitleManager extends SubtitleManager {

    SRTSubtitleManager(File file) {
        super(file, new SRTTimeParser());
    }
}
