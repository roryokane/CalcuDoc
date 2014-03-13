package com.roryokane.calcudoc;

import com.google.common.base.Joiner;

import java.util.List;

public class StringLinesConversion {
    static String[] splitStringIntoLines(String string) {
        return splitStringIntoLines(string, true);
    }

    private static String[] splitStringIntoLines(String string, boolean allowTrailingBlankLines) {
        final int limit = allowTrailingBlankLines ? -1 : 0;
        return string.split("\r\n|\r|\n", limit);
    }

    static String joinLinesIntoString(List<String> lines) {
        return Joiner.on("\n").join(lines);
    }
}
