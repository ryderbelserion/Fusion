package com.ryderbelserion.core.util;

import com.ryderbelserion.core.FusionLayout;
import com.ryderbelserion.core.FusionProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class StringUtils {

    private static final FusionLayout api = FusionProvider.get();

    public StringUtils() {
        throw new AssertionError();
    }

    public static String fromInteger(final int number) {
        return NumberFormat.getIntegerInstance(Locale.US).format(number);
    }

    public static String fromDouble(final double number) {
        return NumberFormat.getNumberInstance(Locale.US).format(number);
    }

    public static @NotNull Component parse(@NotNull final String message, @NotNull final TagResolver... tags) {
        if (message.isEmpty()) return Component.empty();

        return MiniMessage.miniMessage().deserialize(message, tags).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    public static @NotNull Component parse(@NotNull final String message) {
        return parse(message, TagResolver.empty());
    }

    public static @NotNull Component toComponent(@NotNull final String component) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(component.replace("§", "&"));
    }

    public static @NotNull List<Component> toComponent(@NotNull final List<String> lore) {
        return new ArrayList<>(lore.size()) {{
            lore.forEach(line -> add(toComponent(line)));
        }};
    }

    public static @NotNull String fromComponent(@NotNull final Component component) {
        return fromComponent(component, false);
    }

    public static @NotNull String fromComponent(@NotNull final Component component, final boolean isMessage) {
        final String value = MiniMessage.miniMessage().serialize(component);

        return isMessage ? value.replace("\\<", "<") : value;
    }

    public static @NotNull List<String> fromComponent(@NotNull final List<Component> components) {
        final List<String> keys = new ArrayList<>(components.size());

        components.forEach(component -> keys.add(fromComponent(component)));

        return keys;
    }

    public static @NotNull String convert(@NotNull final String component) {
        return convert(component, false);
    }

    public static @NotNull List<String> convert(@NotNull final List<String> components) {
        return convert(components, false);
    }

    public static @NotNull List<String> convert(@NotNull final List<String> components, final boolean isMessage) {
        return new ArrayList<>(components.size()) {{
            components.forEach(line -> add(convert(line, isMessage)));
        }};
    }

    public static @NotNull String convert(@NotNull final String component, final boolean isMessage) {
        return fromComponent(toComponent(component), isMessage);
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