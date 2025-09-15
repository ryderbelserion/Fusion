package com.ryderbelserion.fusion.core.api.interfaces;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

public interface IStringUtils {

    default String replaceAllBrackets(@NotNull final String input) {
        return replaceAngleBrackets(replaceBrackets(input));
    }

    String toString(@NotNull final List<String> list);

    Component parseComponent(@NotNull final String message, @NotNull final TagResolver... tags);

    Component parseComponent(@NotNull final String message);

    String fromComponent(@NotNull final Component component, final boolean isMessage);

    default String fromComponent(@NotNull final Component component) {
        return fromComponent(component, false);
    }

    List<String> fromComponent(@NotNull final List<Component> components);

    Component fromLegacy(@NotNull final String component);

    List<Component> fromLegacy(@NotNull final List<String> lore);

    List<String> convertLegacy(@NotNull final List<String> components, final boolean isMessage);

    default List<String> convertLegacy(@NotNull final List<String> components) {
        return convertLegacy(components, false);
    }

    default String convertLegacy(@NotNull final String component) {
        return convertLegacy(component, false);
    }

    String convertLegacy(@NotNull final String component, final boolean isMessage);

    String replaceAngleBrackets(@NotNull final String input);

    String replaceBrackets(@NotNull final String input);

    Optional<Number> tryParseInt(@NotNull final String input);

    Optional<Boolean> tryParseBoolean(@NotNull final String input);

    String fromInteger(final int input);

    String fromDouble(final double input);

    String format(final double input);

    RoundingMode getRoundingMode();

}