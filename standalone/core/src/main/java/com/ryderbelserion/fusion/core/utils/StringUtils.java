package com.ryderbelserion.fusion.core.utils;

import com.ryderbelserion.fusion.api.FusionProvider;
import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.files.utils.FileUtils;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

@NullMarked
public class StringUtils {

    private static final Pattern BRACKET_PATTERN = Pattern.compile("\\{(.*?)}");
    private static final Pattern ANGLE_PATTERN = Pattern.compile("[<>]");

    private static final FusionCore fusion = (FusionCore) FusionProvider.api();

    public static List<String> getStringList(final CommentedConfigurationNode node, final String defaultValue) {
        return FileUtils.getStringList(node, List.of(defaultValue));
    }

    public static List<String> getStringList(final CommentedConfigurationNode node) {
        return FileUtils.getStringList(node, List.of());
    }

    public static List<String> getStringList(final BasicConfigurationNode node, final List<String> defaultValues) {
        return FileUtils.getStringList(node, defaultValues);
    }

    public static List<String> getStringList(final BasicConfigurationNode node, final String defaultValue) {
        return getStringList(node, List.of(defaultValue));
    }

    public static List<String> getStringList(final BasicConfigurationNode node) {
        return FileUtils.getStringList(node, List.of());
    }

    public static Optional<Boolean> tryParseBoolean(final String value) {
        try {
            return Optional.of(Boolean.parseBoolean(value));
        } catch (final NumberFormatException exception) {
            return Optional.empty();
        }
    }

    public static Optional<Number> tryParseInt(final String value) {
        try {
            return Optional.of(Integer.parseInt(value));
        } catch (final NumberFormatException exception) {
            return Optional.empty();
        }
    }

    public static String replaceAngleBrackets(final String input) {
        return ANGLE_PATTERN.matcher(input).replaceAll("");
    }

    public static String replaceAllBrackets(final String input) {
        return replaceAngleBrackets(replaceBrackets(input));
    }

    public static String replaceBrackets(final String input) {
        return BRACKET_PATTERN.matcher(input).replaceAll("<$1>");
    }

    public static String fromInteger(final int number) {
        return NumberFormat.getIntegerInstance(Locale.US).format(number);
    }

    public static String fromDouble(final double number) {
        return NumberFormat.getNumberInstance(Locale.US).format(number);
    }

    public static String formatNumber(final double number, final NumberFormat.Style style) {
        return NumberFormat.getCompactNumberInstance(Locale.US, style).format(number);
    }

    public static String formatNumber(final double number) {
        return formatNumber(number, NumberFormat.Style.SHORT);
    }

    public static String format(final double number) {
        final DecimalFormat decimalFormat = new DecimalFormat(fusion.getNumberFormat());

        decimalFormat.setRoundingMode(mode());

        return decimalFormat.format(number);
    }

    public static RoundingMode mode() {
        return RoundingMode.valueOf(fusion.getRounding().toUpperCase());
    }

    public static String toString(final List<String> list) {
        return FileUtils.toString(list);
    }

    public static String chomp(final String value) {
        return FileUtils.chomp(value);
    }
}