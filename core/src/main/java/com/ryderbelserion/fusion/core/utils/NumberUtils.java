package com.ryderbelserion.fusion.core.utils;

import com.ryderbelserion.fusion.core.FusionCore;
import org.jetbrains.annotations.NotNull;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;

public class NumberUtils {

    private static final FusionCore api = FusionCore.Provider.get();

    public static String fromInteger(final int number) {
        return NumberFormat.getIntegerInstance(Locale.US).format(number);
    }

    public static String fromDouble(final double number) {
        return NumberFormat.getNumberInstance(Locale.US).format(number);
    }

    public static Optional<Number> tryParseInt(@NotNull final String value) {
        try {
            return Optional.of(Integer.parseInt(value));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    public static Optional<Boolean> tryParseBoolean(@NotNull final String value) {
        try {
            return Optional.of(Boolean.parseBoolean(value));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    public static String format(final double value) {
        final DecimalFormat decimalFormat = new DecimalFormat(api.getNumberFormat());

        decimalFormat.setRoundingMode(mode());

        return decimalFormat.format(value);
    }

    public static RoundingMode mode() {
        return RoundingMode.valueOf(api.getRoundingFormat().toUpperCase());
    }
}