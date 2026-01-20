package com.ryderbelserion.fusion.core.utils;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.files.FileException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class StringUtils {

    private static final Pattern BRACKET_PATTERN = Pattern.compile("\\{(.*?)}");
    private static final Pattern ANGLE_PATTERN = Pattern.compile("[<>]");

    private static final FusionCore fusion = FusionProvider.getInstance();

    private static final char LF = '\n';
    private static final char CR = '\r';

    public static @NotNull List<String> getStringList(@NotNull final CommentedConfigurationNode node, @NotNull final String defaultValue) {
        try {
            final List<String> list = node.getList(String.class);

            return list != null ? list : List.of(defaultValue);
        } catch (SerializationException exception) {
            throw new FileException(String.format("Failed to serialize %s!", node.path()), exception);
        }
    }

    public static @NotNull Optional<Boolean> tryParseBoolean(@NotNull final String value) {
        try {
            return Optional.of(Boolean.parseBoolean(value));
        } catch (final NumberFormatException exception) {
            return Optional.empty();
        }
    }

    public static @NotNull Optional<Number> tryParseInt(@NotNull final String value) {
        try {
            return Optional.of(Integer.parseInt(value));
        } catch (final NumberFormatException exception) {
            return Optional.empty();
        }
    }

    public static @NotNull String replaceAngleBrackets(@NotNull final String input) {
        return ANGLE_PATTERN.matcher(input).replaceAll("");
    }

    public static @NotNull String replaceAllBrackets(@NotNull final String input) {
        return replaceAngleBrackets(replaceBrackets(input));
    }

    public static @NotNull String replaceBrackets(@NotNull final String input) {
        return BRACKET_PATTERN.matcher(input).replaceAll("<$1>");
    }

    public static @NotNull String toString(@NotNull final List<String> list) {
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