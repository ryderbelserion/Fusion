package com.ryderbelserion.fusion.paper.utils;

import com.ryderbelserion.fusion.paper.FusionProvider;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.inventory.CraftContainer;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ColorUtils {

    public static void updateTitle(@NotNull final Player player, @NotNull final String title, @NotNull final Map<String, String> placeholders) {
        final ServerPlayer entityPlayer = (ServerPlayer) ((CraftEntity) player).getHandle();

        final int containerId = entityPlayer.containerMenu.containerId;

        final MenuType<?> windowType = CraftContainer.getNotchInventoryType(player.getOpenInventory().getTopInventory());

        entityPlayer.connection.send(new ClientboundOpenScreenPacket(containerId, windowType, CraftChatMessage.fromJSON(JSONComponentSerializer.json().serialize(
                FusionProvider.getInstance().parse(player, title, placeholders)))));

        player.updateInventory();
    }

    public static void updateTitle(@NotNull final Player player, @NotNull final String title) {
        updateTitle(player, title, new HashMap<>());
    }

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

        if (rgb.length != 3) {
            return null;
        }

        final int red = Integer.parseInt(rgb[0]);
        final int green = Integer.parseInt(rgb[1]);
        final int blue = Integer.parseInt(rgb[2]);

        return Color.fromRGB(red, green, blue);
    }
}