package com.ryderbelserion.fusion.api.utils;

import com.ryderbelserion.fusion.api.FusionApi;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class StringUtils {

    private static final FusionApi api = FusionApi.Provider.get();

    public static String fromInteger(final int number) {
        return NumberFormat.getIntegerInstance(Locale.US).format(number);
    }

    public static String fromDouble(final double number) {
        return NumberFormat.getNumberInstance(Locale.US).format(number);
    }

    public static Optional<Number> tryParseInt(final String value) {
        try {
            return Optional.of(Integer.parseInt(value));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    public static Optional<Boolean> tryParseBoolean(final String value) {
        try {
            return Optional.of(Boolean.parseBoolean(value));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    public static String toString(final List<String> list) {
        if (list.isEmpty()) return "";

        final StringBuilder message = new StringBuilder();

        for (final String line : list) {
            message.append(line).append("\n");
        }

        return api.chomp(message.toString());
    }

    public static String format(final double value) {
        final DecimalFormat decimalFormat = new DecimalFormat(api.getNumberFormat());

        decimalFormat.setRoundingMode(mode());

        return decimalFormat.format(value);
    }

    public static RoundingMode mode() {
        return RoundingMode.valueOf(api.getRounding().toUpperCase());
    }
}