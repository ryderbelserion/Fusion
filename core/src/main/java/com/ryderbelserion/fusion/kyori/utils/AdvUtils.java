package com.ryderbelserion.fusion.kyori.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class AdvUtils {

    public static @NotNull Component parse(@NotNull String message, @NotNull TagResolver... tags) {
        if (message.isEmpty()) return Component.empty();

        return MiniMessage.miniMessage().deserialize(message, tags).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    public static @NotNull Component parse(@NotNull String message) {
        return parse(message, TagResolver.empty());
    }

    public static @NotNull String fromComponent(@NotNull Component component) {
        return fromComponent(component, false);
    }

    public static @NotNull String fromComponent(@NotNull Component component,  boolean isMessage) {
        String value = MiniMessage.miniMessage().serialize(component);

        return isMessage ? value.replace("\\<", "<") : value;
    }

    public static @NotNull List<String> fromComponent(@NotNull List<Component> components) {
        List<String> keys = new ArrayList<>(components.size());

        components.forEach(component -> keys.add(fromComponent(component)));

        return keys;
    }

    public static @NotNull Component toComponent(@NotNull String component) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(component.replace("§", "&"));
    }

    public static @NotNull List<Component> toComponent(@NotNull List<String> lore) {
        return new ArrayList<>(lore.size()) {{
            lore.forEach(line -> add(toComponent(line)));
        }};
    }

    public static @NotNull String convert(@NotNull String component) {
        return convert(component, false);
    }

    public static @NotNull List<String> convert(@NotNull List<String> components) {
        return convert(components, false);
    }

    public static @NotNull List<String> convert(@NotNull List<String> components, boolean isMessage) {
        return new ArrayList<>(components.size()) {{
            components.forEach(line -> add(convert(line, isMessage)));
        }};
    }

    public static @NotNull String convert(@NotNull String component, boolean isMessage) {
        return fromComponent(toComponent(component), isMessage);
    }
}