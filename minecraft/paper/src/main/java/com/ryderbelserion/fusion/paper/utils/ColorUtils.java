package com.ryderbelserion.fusion.paper.utils;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.jspecify.annotations.NullMarked;
import java.util.Optional;

@NullMarked
public class ColorUtils {

    public static DyeColor getDyeColor(final String value) {
        if (value.isEmpty()) return DyeColor.WHITE;

        return switch (value.toLowerCase()) {
            case "orange" -> DyeColor.ORANGE;
            case "magenta", "fuchsia" -> DyeColor.MAGENTA;
            case "light_blue", "aqua" -> DyeColor.LIGHT_BLUE;
            case "yellow" -> DyeColor.YELLOW;
            case "lime" -> DyeColor.LIME;
            case "pink" -> DyeColor.PINK;
            case "gray" -> DyeColor.GRAY;
            case "light_gray", "silver" -> DyeColor.LIGHT_GRAY;
            case "cyan", "teal" -> DyeColor.CYAN;
            case "purple" -> DyeColor.PURPLE;
            case "blue", "navy" -> DyeColor.BLUE;
            case "brown" -> DyeColor.BROWN;
            case "green", "olive" -> DyeColor.GREEN;
            case "red", "maroon" -> DyeColor.RED;
            case "black" -> DyeColor.BLACK;
            default -> DyeColor.WHITE;
        };
    }

    public static Color getColor(final String value) {
        if (value.isEmpty()) return Color.WHITE;

        return switch (value.toLowerCase()) {
            case "aqua" -> Color.AQUA;
            case "black" -> Color.BLACK;
            case "blue" -> Color.BLUE;
            case "fuchsia" -> Color.FUCHSIA;
            case "gray" -> Color.GRAY;
            case "green" -> Color.GREEN;
            case "lime" -> Color.LIME;
            case "maroon" -> Color.MAROON;
            case "navy" -> Color.NAVY;
            case "olive" -> Color.OLIVE;
            case "orange" -> Color.ORANGE;
            case "purple" -> Color.PURPLE;
            case "red" -> Color.RED;
            case "silver" -> Color.SILVER;
            case "teal" -> Color.TEAL;
            case "yellow" -> Color.YELLOW;
            default -> Color.WHITE;
        };
    }

    public static Optional<Color> getRGB(final String color) {
        if (color.isEmpty()) return Optional.empty();

        if (!color.contains(",")) {
            return Optional.of(getColor(color));
        }

        final String[] rgb = color.split(",");

        if (rgb.length != 3) return Optional.empty();

        final int red = Integer.parseInt(rgb[0]);
        final int green = Integer.parseInt(rgb[1]);
        final int blue = Integer.parseInt(rgb[2]);

        return Optional.of(Color.fromRGB(red, green, blue));
    }
}