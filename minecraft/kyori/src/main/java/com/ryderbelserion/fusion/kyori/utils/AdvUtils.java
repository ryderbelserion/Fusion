package com.ryderbelserion.fusion.kyori.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jspecify.annotations.NullMarked;
import java.util.ArrayList;
import java.util.List;

@NullMarked
public class AdvUtils {

    public static Component parse(final String message, final TagResolver... tags) {
        if (message.isEmpty()) return Component.empty();

        return MiniMessage.miniMessage().deserialize(message, tags).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    public static Component parse(final String message) {
        return parse(message, TagResolver.empty());
    }

    public static String fromComponent(final Component component) {
        return fromComponent(component, false);
    }

    public static String fromComponent(final Component component, final boolean isMessage) {
        final String value = MiniMessage.miniMessage().serialize(component);

        return isMessage ? value.replace("\\<", "<") : value;
    }

    public static List<String> fromComponent(final List<Component> components) {
        final List<String> keys = new ArrayList<>(components.size());

        components.forEach(component -> keys.add(fromComponent(component)));

        return keys;
    }

    public static Component toComponent(final String component) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(component.replace("§", "&"));
    }

    public static List<Component> toComponent(final List<String> lore) {
        return new ArrayList<>(lore.size()) {{
            lore.forEach(line -> add(toComponent(line)));
        }};
    }

    public static String convert(final String component) {
        return convert(component, false);
    }

    public static List<String> convert(final List<String> components) {
        return convert(components, false);
    }

    public static List<String> convert(final List<String> components, final boolean isMessage) {
        return new ArrayList<>(components.size()) {{
            components.forEach(line -> add(convert(line, isMessage)));
        }};
    }

    public static String convert(final String component, final boolean isMessage) {
        return fromComponent(toComponent(component), isMessage);
    }
}