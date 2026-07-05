package com.ryderbelserion.fusion.core.utils;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.files.FileException;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

public class StringUtils {

    private static final Pattern BRACKET_PATTERN = Pattern.compile("\\{(.*?)}");
    private static final Pattern ANGLE_PATTERN = Pattern.compile("[<>]");

    private static final FusionCore fusion = FusionProvider.getInstance();

    private static final char LF = '\n';
    private static final char CR = '\r';

    public static @NonNull String replacePlaceholders(@NonNull final String message, @NonNull final Map<String, String> placeholders) {
        String safeMessage = message;

        if (!placeholders.isEmpty()) {
            for (final Map.Entry<String, String> key : placeholders.entrySet()) {
                if (key == null) continue;

                final String placeholder = key.getKey();
                final String value = key.getValue();

                if (placeholder != null && value != null) {
                    safeMessage = safeMessage.replace(placeholder, value).replace(placeholder.toLowerCase(), value);
                }
            }
        }

        return safeMessage;
    }

    public static @NonNull List<String> getStringList(@NonNull final CommentedConfigurationNode node, @NonNull final List<String> defaultValues) {
        try {
            final List<String> list = node.getList(String.class);

            return list != null ? list : defaultValues;
        } catch (SerializationException exception) {
            throw new FileException(String.format("Failed to serialize %s!", node.path()), exception);
        }
    }

    public static @NonNull List<String> getStringList(@NonNull final CommentedConfigurationNode node, @NonNull final String defaultValue) {
        return getStringList(node, List.of(defaultValue));
    }

    public static @NonNull List<String> getStringList(@NonNull final CommentedConfigurationNode node) {
        return getStringList(node, List.of());
    }

    public static @NonNull List<String> getStringList(@NonNull final BasicConfigurationNode node, @NonNull final List<String> defaultValues) {
        try {
            final List<String> list = node.getList(String.class);

            return list != null ? list : defaultValues;
        } catch (SerializationException exception) {
            throw new FileException(String.format("Failed to serialize %s!", node.path()), exception);
        }
    }

    public static @NonNull List<String> getStringList(@NonNull final BasicConfigurationNode node, @NonNull final String defaultValue) {
        return getStringList(node, List.of(defaultValue));
    }

    public static @NonNull List<String> getStringList(@NonNull final BasicConfigurationNode node) {
        return getStringList(node, List.of());
    }

    public static @NonNull Optional<Boolean> tryParseBoolean(@NonNull final String value) {
        try {
            return Optional.of(Boolean.parseBoolean(value));
        } catch (final NumberFormatException exception) {
            return Optional.empty();
        }
    }

    public static @NonNull Optional<Number> tryParseInt(@NonNull final String value) {
        try {
            return Optional.of(Integer.parseInt(value));
        } catch (final NumberFormatException exception) {
            return Optional.empty();
        }
    }

    public static @NonNull String replaceAngleBrackets(@NonNull final String input) {
        return ANGLE_PATTERN.matcher(input).replaceAll("");
    }

    public static @NonNull String replaceAllBrackets(@NonNull final String input) {
        return replaceAngleBrackets(replaceBrackets(input));
    }

    public static @NonNull String replaceBrackets(@NonNull final String input) {
        return BRACKET_PATTERN.matcher(input).replaceAll("<$1>");
    }

    public static @NonNull String fromInteger(final int number) {
        return NumberFormat.getIntegerInstance(Locale.US).format(number);
    }

    public static @NonNull String fromDouble(final double number) {
        return NumberFormat.getNumberInstance(Locale.US).format(number);
    }

    public static @NonNull String formatNumber(final double number, final NumberFormat.@NonNull Style style) {
        return NumberFormat.getCompactNumberInstance(Locale.US, style).format(number);
    }

    public static @NonNull String formatNumber(final double number) {
        return formatNumber(number, NumberFormat.Style.SHORT);
    }

    public static @NonNull String format(final double number) {
        final DecimalFormat decimalFormat = new DecimalFormat(fusion.getNumberFormat());

        decimalFormat.setRoundingMode(mode());

        return decimalFormat.format(number);
    }

    public static @NonNull RoundingMode mode() {
        return RoundingMode.valueOf(fusion.getRounding().toUpperCase());
    }

    public static @NonNull String toString(@NonNull final List<String> list) {
        if (list.isEmpty()) return "";

        final StringBuilder message = new StringBuilder(list.size());

        for (final String line : list) {
            message.append(line).append("\n");
        }

        return chomp(message.toString());
    }

    public static String chomp(@Nullable final String value) {
        if (value == null || value.isEmpty()) {
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