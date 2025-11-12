package com.ryderbelserion.fusion.core.utils;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.FusionProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

public class StringUtils {

    private static final Pattern BRACKET_PATTERN = Pattern.compile("\\{(.*?)}");
    private static final Pattern ANGLE_PATTERN = Pattern.compile("[<>]");

    private static final FusionCore fusion = FusionProvider.getInstance();

    private static final char LF = '\n';
    private static final char CR = '\r';

    public static @NotNull List<String> convertLegacy(@NotNull final List<String> components, final boolean isMessage) {
        return new ArrayList<>(components.size()) {{
            components.forEach(line -> add(convertLegacy(line, isMessage)));
        }};
    }

    public static @NotNull Component parseComponent(@NotNull final String message, @NotNull final TagResolver... tags) {
        if (message.isEmpty()) return Component.empty();

        return MiniMessage.miniMessage().deserialize(message, tags).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    public static @NotNull String fromComponent(@NotNull final Component component, final boolean isMessage) {
        final String value = MiniMessage.miniMessage().serialize(component);

        return isMessage ? value.replace("\\<", "<") : value;
    }

    public static @NotNull String convertLegacy(@NotNull final String component, final boolean isMessage) {
        return fromComponent(fromLegacy(component), isMessage);
    }

    public static @NotNull List<String> fromComponent(@NotNull final List<Component> components) {
        final List<String> keys = new ArrayList<>(components.size());

        components.forEach(component -> keys.add(fromComponent(component)));

        return keys;
    }

    public static @NotNull List<String> convertLegacy(@NotNull final List<String> components) {
        return convertLegacy(components, false);
    }

    public static @NotNull Optional<Boolean> tryParseBoolean(@NotNull final String value) {
        try {
            return Optional.of(Boolean.parseBoolean(value));
        } catch (final NumberFormatException exception) {
            return Optional.empty();
        }
    }

    public static @NotNull List<Component> fromLegacy(@NotNull final List<String> lore) {
        return new ArrayList<>(lore.size()) {{
            lore.forEach(line -> add(fromLegacy(line)));
        }};
    }

    public static @NotNull Optional<Number> tryParseInt(@NotNull final String value) {
        try {
            return Optional.of(Integer.parseInt(value));
        } catch (final NumberFormatException exception) {
            return Optional.empty();
        }
    }

    public static @NotNull String fromComponent(@NotNull final Component component) {
        return fromComponent(component, false);
    }

    public static @NotNull String replaceAngleBrackets(@NotNull final String input) {
        return ANGLE_PATTERN.matcher(input).replaceAll("");
    }

    public static @NotNull Component parseComponent(@NotNull final String message) {
        return parseComponent(message, TagResolver.empty());
    }

    public static @NotNull String convertLegacy(@NotNull final String component) {
        return convertLegacy(component, false);
    }

    public static @NotNull Component fromLegacy(@NotNull final String component) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(component.replace("§", "&"));
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

    public static @NotNull String fromInteger(final int number) {
        return NumberFormat.getIntegerInstance(Locale.US).format(number);
    }

    public static @NotNull String fromDouble(final double number) {
        return NumberFormat.getNumberInstance(Locale.US).format(number);
    }

    public static @NotNull String format(final double value) {
        final DecimalFormat decimalFormat = new DecimalFormat(fusion.getNumberFormat());

        decimalFormat.setRoundingMode(getRoundingMode());

        return decimalFormat.format(value);
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

    public static @NotNull RoundingMode getRoundingMode() {
        return RoundingMode.valueOf(fusion.getRounding().toUpperCase());
    }
}