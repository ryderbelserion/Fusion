package com.ryderbelserion.fusion.core.utils;

import com.ryderbelserion.fusion.api.FusionProvider;
import com.ryderbelserion.fusion.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.FusionCore;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

@NullMarked
public class StringUtils {

    private static final Pattern BRACKET_PATTERN = Pattern.compile("\\{(.*?)}");
    private static final Pattern ANGLE_PATTERN = Pattern.compile("[<>]");

    private static final FusionCore fusion = (FusionCore) FusionProvider.api();

    private static final char LF = '\n';
    private static final char CR = '\r';

    public static List<String> getStringList(final CommentedConfigurationNode node, final List<String> defaultValues) {
        try {
            final List<String> list = node.getList(String.class);

            return list != null ? list : defaultValues;
        } catch (SerializationException exception) {
            throw new FusionException(String.format("Failed to serialize %s!", node.path()), exception);
        }
    }

    public static List<String> getStringList(final CommentedConfigurationNode node, final String defaultValue) {
        return getStringList(node, List.of(defaultValue));
    }

    public static List<String> getStringList(final CommentedConfigurationNode node) {
        return getStringList(node, List.of());
    }

    public static List<String> getStringList(final BasicConfigurationNode node, final List<String> defaultValues) {
        try {
            final List<String> list = node.getList(String.class);

            return list != null ? list : defaultValues;
        } catch (SerializationException exception) {
            throw new FusionException(String.format("Failed to serialize %s!", node.path()), exception);
        }
    }

    public static List<String> getStringList(final BasicConfigurationNode node, final String defaultValue) {
        return getStringList(node, List.of(defaultValue));
    }

    public static List<String> getStringList(final BasicConfigurationNode node) {
        return getStringList(node, List.of());
    }

    public static Optional<Boolean> tryParseBoolean(final String value) {
        try {
            return Optional.of(Boolean.parseBoolean(value));
        } catch (final NumberFormatException exception) {
            return Optional.empty();
        }
    }

    public static Optional<Number> tryParseInt(final String value) {
        try {
            return Optional.of(Integer.parseInt(value));
        } catch (final NumberFormatException exception) {
            return Optional.empty();
        }
    }

    public static String replaceAngleBrackets(final String input) {
        return ANGLE_PATTERN.matcher(input).replaceAll("");
    }

    public static String replaceAllBrackets(final String input) {
        return replaceAngleBrackets(replaceBrackets(input));
    }

    public static String replaceBrackets(final String input) {
        return BRACKET_PATTERN.matcher(input).replaceAll("<$1>");
    }

    public static String fromInteger(final int number) {
        return NumberFormat.getIntegerInstance(Locale.US).format(number);
    }

    public static String fromDouble(final double number) {
        return NumberFormat.getNumberInstance(Locale.US).format(number);
    }

    public static String formatNumber(final double number, final NumberFormat.Style style) {
        return NumberFormat.getCompactNumberInstance(Locale.US, style).format(number);
    }

    public static String formatNumber(final double number) {
        return formatNumber(number, NumberFormat.Style.SHORT);
    }

    public static String format(final double number) {
        final DecimalFormat decimalFormat = new DecimalFormat(fusion.getNumberFormat());

        decimalFormat.setRoundingMode(mode());

        return decimalFormat.format(number);
    }

    public static RoundingMode mode() {
        return RoundingMode.valueOf(fusion.getRounding().toUpperCase());
    }

    public static String toString(final List<String> list) {
        if (list.isEmpty()) return "";

        final StringBuilder message = new StringBuilder(list.size());

        for (final String line : list) {
            message.append(line).append("\n");
        }

        return chomp(message.toString());
    }

    public static String chomp(final String value) {
        if (value.isEmpty()) {
            return value;
        }

        if (value.length() == 1) {
            final char character = value.charAt(0);

            return character == CR || character == LF ? "" : value;
        }

        int lastIdx = value.length() - 1;

        final char last = value.charAt(lastIdx);

        if (last == LF) {
            if (value.charAt(lastIdx - 1) == CR) {
                lastIdx--;
            }
        } else if (last != CR) {
            lastIdx++;
        }

        return value.substring(0, lastIdx);
    }
}