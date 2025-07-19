package com.ryderbelserion.fusion.core.api.interfaces;

import org.jetbrains.annotations.NotNull;

public interface IStringUtils {

    default String replaceAllBrackets(@NotNull final String input) {
        return replaceAngleBrackets(replaceBrackets(input));
    }

    String replaceAngleBrackets(@NotNull final String input);

    String replaceBrackets(@NotNull final String input);

}