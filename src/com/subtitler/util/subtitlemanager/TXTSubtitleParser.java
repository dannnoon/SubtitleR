package com.subtitler.util.subtitlemanager;

import com.subtitler.util.timeparser.TXTTimeParser;

import java.io.File;

class TXTSubtitleParser extends SubtitleManager {

    TXTSubtitleParser(File file) {
        super(file, new TXTTimeParser());
    }
}
