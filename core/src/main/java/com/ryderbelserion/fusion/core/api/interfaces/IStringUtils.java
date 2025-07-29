package com.ryderbelserion.fusion.core.api.interfaces;

import org.jetbrains.annotations.NotNull;
import java.math.RoundingMode;
import java.util.Optional;

public interface IStringUtils {

    default String replaceAllBrackets(@NotNull final String input) {
        return replaceAngleBrackets(replaceBrackets(input));
    }

    String replaceAngleBrackets(@NotNull final String input);

    String replaceBrackets(@NotNull final String input);

    Optional<Number> tryParseInt(@NotNull final String input);

    Optional<Boolean> tryParseBoolean(@NotNull final String input);

    String fromInteger(final int input);

    String fromDouble(final double input);

    String format(final double input);

    RoundingMode getRoundingMode();

}