package com.ryderbelserion.fusion.kyori.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jspecify.annotations.NonNull;
import java.util.ArrayList;
import java.util.List;

public class AdvUtils {

    public static @NonNull Component parse(@NonNull final String message, @NonNull final TagResolver... tags) {
        if (message.isEmpty()) return Component.empty();

        return MiniMessage.miniMessage().deserialize(message, tags).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    public static @NonNull Component parse(@NonNull final String message) {
        return parse(message, TagResolver.empty());
    }

    public static @NonNull String fromComponent(@NonNull final Component component) {
        return fromComponent(component, false);
    }

    public static @NonNull String fromComponent(@NonNull final Component component, final boolean isMessage) {
        final String value = MiniMessage.miniMessage().serialize(component);

        return isMessage ? value.replace("\\<", "<") : value;
    }

    public static @NonNull List<String> fromComponent(@NonNull final List<Component> components) {
        final List<String> keys = new ArrayList<>(components.size());

        components.forEach(component -> keys.add(fromComponent(component)));

        return keys;
    }

    public static @NonNull Component toComponent(@NonNull final String component) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(component.replace("§", "&"));
    }

    public static @NonNull List<Component> toComponent(@NonNull final List<String> lore) {
        return new ArrayList<>(lore.size()) {{
            lore.forEach(line -> add(toComponent(line)));
        }};
    }

    public static @NonNull String convert(@NonNull final String component) {
        return convert(component, false);
    }

    public static @NonNull List<String> convert(@NonNull final List<String> components) {
        return convert(components, false);
    }

    public static @NonNull List<String> convert(@NonNull final List<String> components, final boolean isMessage) {
        return new ArrayList<>(components.size()) {{
            components.forEach(line -> add(convert(line, isMessage)));
        }};
    }

    public static @NonNull String convert(@NonNull final String component, final boolean isMessage) {
        return fromComponent(toComponent(component), isMessage);
    }
}