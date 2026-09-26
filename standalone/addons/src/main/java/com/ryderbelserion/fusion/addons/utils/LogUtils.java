package com.ryderbelserion.fusion.addons.utils;

import org.tinylog.Logger;

public class LogUtils {

    public static void info(final String message, final Object... placeholders) {
        Logger.info(String.format(message, placeholders));
    }

    public static void warn(final String message, final Object... placeholders) {
        Logger.warn(String.format(message, placeholders));
    }

    public static void error(final String message, final Object... placeholders) {
        Logger.error(String.format(message, placeholders));
    }
}