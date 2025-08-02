package com.ryderbelserion.fusion.core.utils;

import com.ryderbelserion.fusion.core.FusionConfig;
import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.interfaces.IStringUtils;
import org.jetbrains.annotations.NotNull;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

public class StringUtils implements IStringUtils {

    private static final Pattern BRACKET_PATTERN = Pattern.compile("\\{(.*?)}");
    private static final Pattern ANGLE_PATTERN = Pattern.compile("[<>]");

    private final FusionCore fusion;
    private final FusionConfig config;

    public StringUtils(@NotNull final FusionCore fusion) {
        this.fusion = fusion;
        this.config = this.fusion.getConfig();
    }

    public @NotNull String toString(@NotNull final List<String> list) {
        if (list.isEmpty()) return "";

        final StringBuilder message = new StringBuilder(list.size());

        for (final String line : list) {
            message.append(line).append("\n");
        }

        return this.fusion.chomp(message.toString());
    }

    @Override
    public String replaceAngleBrackets(@NotNull final String input) {
        return ANGLE_PATTERN.matcher(input).replaceAll("");
    }

    @Override
    public String replaceBrackets(@NotNull final String input) {
        return BRACKET_PATTERN.matcher(input).replaceAll("<$1>");
    }

    @Override
    public @NotNull Optional<Number> tryParseInt(@NotNull final String value) {
        try {
            return Optional.of(Integer.parseInt(value));
        } catch (final NumberFormatException exception) {
            return Optional.empty();
        }
    }

    @Override
    public @NotNull Optional<Boolean> tryParseBoolean(@NotNull final String value) {
        try {
            return Optional.of(Boolean.parseBoolean(value));
        } catch (final NumberFormatException exception) {
            return Optional.empty();
        }
    }

    @Override
    public @NotNull String fromInteger(final int number) {
        return NumberFormat.getIntegerInstance(Locale.US).format(number);
    }

    @Override
    public @NotNull String fromDouble(final double number) {
        return NumberFormat.getNumberInstance(Locale.US).format(number);
    }

    @Override
    public @NotNull String format(final double value) {
        final DecimalFormat decimalFormat = new DecimalFormat(this.config.getNumberFormat());

        decimalFormat.setRoundingMode(getRoundingMode());

        return decimalFormat.format(value);
    }

    @Override
    public @NotNull RoundingMode getRoundingMode() {
        return RoundingMode.valueOf(this.config.getRoundingFormat().toUpperCase());
    }
}