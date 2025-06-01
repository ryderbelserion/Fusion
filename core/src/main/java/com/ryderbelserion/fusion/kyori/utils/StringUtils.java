package com.ryderbelserion.fusion.kyori.utils;

import com.ryderbelserion.fusion.kyori.FusionKyori;
import com.ryderbelserion.fusion.core.FusionCore;
import org.jetbrains.annotations.NotNull;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

public class StringUtils {

    private static final Pattern BRACKET_PATTERN = Pattern.compile("\\{(.*?)}");
    private static final Pattern ANGLE_PATTERN = Pattern.compile("[<>]");

    private static final FusionKyori kyori = (FusionKyori) FusionCore.Provider.get();

    public static @NotNull Optional<Number> tryParseInt(@NotNull final String value) {
        try {
            return Optional.of(Integer.parseInt(value));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    public static @NotNull Optional<Boolean> tryParseBoolean(@NotNull final String value) {
        try {
            return Optional.of(Boolean.parseBoolean(value));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    public static @NotNull String replaceAllBrackets(@NotNull String input) {
        return replaceAngle(replaceBrackets(input));
    }

    public static @NotNull String replaceBrackets(@NotNull String input) {
        return BRACKET_PATTERN.matcher(input).replaceAll("<$1>");
    }

    public static @NotNull String replaceAngle(@NotNull String input) {
        return ANGLE_PATTERN.matcher(input).replaceAll("");
    }

    public static @NotNull String toString(@NotNull List<String> list) {
        if (list.isEmpty()) return "";

        final StringBuilder message = new StringBuilder(list.size());

        for (final String line : list) {
            message.append(line).append("\n");
        }

        return kyori.chomp(message.toString());
    }

    public static @NotNull String fromInteger(final int number) {
        return NumberFormat.getIntegerInstance(Locale.US).format(number);
    }

    public static @NotNull String fromDouble(final double number) {
        return NumberFormat.getNumberInstance(Locale.US).format(number);
    }

    public static @NotNull String format(final double value) {
        final DecimalFormat decimalFormat = new DecimalFormat(kyori.getNumberFormat());

        decimalFormat.setRoundingMode(mode());

        return decimalFormat.format(value);
    }

    public static @NotNull RoundingMode mode() {
        return RoundingMode.valueOf(kyori.getRoundingFormat().toUpperCase());
    }
}