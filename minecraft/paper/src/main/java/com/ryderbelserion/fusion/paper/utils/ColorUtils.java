package com.ryderbelserion.fusion.paper.utils;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ColorUtils {

    public static @NotNull DyeColor getDyeColor(@NotNull final String value) {
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

    public static @NotNull Color getColor(@NotNull final String value) {
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

    public static @Nullable Color getRGB(@NotNull final String color) {
        if (color.isEmpty()) return null;

        final String[] rgb = color.split(",");

        if (rgb.length != 3) return null;

        final int red = Integer.parseInt(rgb[0]);
        final int green = Integer.parseInt(rgb[1]);
        final int blue = Integer.parseInt(rgb[2]);

        return Color.fromRGB(red, green, blue);
    }
}