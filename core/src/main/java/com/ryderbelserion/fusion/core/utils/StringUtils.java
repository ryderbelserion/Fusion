package com.ryderbelserion.fusion.core.utils;

import com.ryderbelserion.fusion.core.api.interfaces.IStringUtils;
import org.jetbrains.annotations.NotNull;
import java.util.regex.Pattern;

public class StringUtils implements IStringUtils {

    private static final Pattern BRACKET_PATTERN = Pattern.compile("\\{(.*?)}");
    private static final Pattern ANGLE_PATTERN = Pattern.compile("[<>]");

    @Override
    public String replaceAngleBrackets(@NotNull final String input) {
        return ANGLE_PATTERN.matcher(input).replaceAll("");
    }

    @Override
    public String replaceBrackets(@NotNull final String input) {
        return BRACKET_PATTERN.matcher(input).replaceAll("<$1>");
    }
}