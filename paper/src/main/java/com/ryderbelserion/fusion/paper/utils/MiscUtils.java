package com.ryderbelserion.fusion.paper.utils;

import org.apache.commons.lang3.StringUtils;
import java.util.List;

public class MiscUtils {

    public static String toString(final List<String> list) {
        if (list.isEmpty()) return "";

        final StringBuilder message = new StringBuilder();

        for (final String line : list) {
            message.append(line).append("\n");
        }

        return StringUtils.chomp(message.toString());
    }
}