package com.ryderbelserion.fusion.paper.util;

import com.ryderbelserion.fusion.paper.FusionApi;
import com.ryderbelserion.fusion.paper.Fusion;
import com.ryderbelserion.fusion.core.util.StringUtils;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.CraftContainer;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PaperMethods {

    private static final Fusion api = FusionApi.get().getFusion();
    private static final ComponentLogger logger = api.getLogger();
    private static final boolean isVerbose = api.isVerbose();

    public static String color(String message) {
        Matcher matcher = Pattern.compile("#[a-fA-F\\d]{6}").matcher(message);
        StringBuilder buffer = new StringBuilder();

        while (matcher.find()) {
            matcher.appendReplacement(buffer, net.md_5.bungee.api.ChatColor.of(matcher.group()).toString());
        }

        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
    }

    public static void updateTitle(@NotNull final Player player, @NotNull final String title) {
        final ServerPlayer entityPlayer = (ServerPlayer) ((CraftHumanEntity) player).getHandle();

        final int containerId = entityPlayer.containerMenu.containerId;

        final MenuType<?> windowType = CraftContainer.getNotchInventoryType(player.getOpenInventory().getTopInventory());

        entityPlayer.connection.send(new ClientboundOpenScreenPacket(containerId, windowType, CraftChatMessage.fromJSON(JSONComponentSerializer.json().serialize(StringUtils.parse(title)))));

        player.updateInventory();
    }

    public static @NotNull RegistryAccess getRegistryAccess() {
        return RegistryAccess.registryAccess();
    }

    public static @Nullable ItemType getItemType(@NotNull final String value) {
        if (value.isEmpty()) {
            if (isVerbose) logger.error("{} cannot be blank when fetching item types.", value);

            return null;
        }

        @Nullable final ItemType key = getRegistryAccess().getRegistry(RegistryKey.ITEM).get(getKey(value));

        if (key == null) {
            if (isVerbose) logger.error("{} is not a valid item type.", value);

            return null;
        }

        return key;
    }

    public static @Nullable Sound getSound(@NotNull final String value) {
        if (value.isEmpty()) {
            if (isVerbose) logger.error("{} cannot be blank when fetching the sound.", value);

            return null;
        }

        @Nullable final Sound key = getRegistryAccess().getRegistry(RegistryKey.SOUND_EVENT).get(getKey(value));

        if (key == null) {
            if (isVerbose) logger.error("{} is not a valid sound.", value);

            return null;
        }

        return key;
    }

    public static @Nullable Enchantment getEnchantment(@NotNull final String value) {
        if (value.isEmpty()) {
            if (isVerbose) logger.error("{} cannot be blank when fetching the enchantment.", value);

            return null;
        }

        @Nullable final Enchantment key = getRegistryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(getKey(value));

        if (key == null) {
            if (isVerbose) logger.error("{} is not a valid enchantment.", value);

            return null;
        }

        return key;
    }

    public static @Nullable TrimPattern getTrimPattern(@NotNull final String value) {
        if (value.isEmpty()) {
            if (isVerbose) logger.error("{} cannot be blank when fetching the trim pattern.", value);

            return null;
        }

        @Nullable final TrimPattern key = getRegistryAccess().getRegistry(RegistryKey.TRIM_PATTERN).get(getKey(value));

        if (key == null) {
            if (isVerbose) logger.error("{} is not a valid trim pattern.", value);

            return null;
        }

        return key;
    }

    public static @Nullable TrimMaterial getTrimMaterial(@NotNull final String value) {
        if (value.isEmpty()) {
            if (isVerbose) logger.error("{} cannot be blank when fetching the trim material.", value);

            return null;
        }

        @Nullable final TrimMaterial key = getRegistryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(getKey(value));

        if (key == null) {
            if (isVerbose) logger.error("{} is not a valid trim material.", value);

            return null;
        }

        return key;
    }

    public static @Nullable PotionType getPotionType(@NotNull final String value) {
        if (value.isEmpty()) {
            if (isVerbose) logger.error("{} cannot be blank when fetching the potion.", value);

            return null;
        }

        @Nullable final PotionType key = getRegistryAccess().getRegistry(RegistryKey.POTION).get(getKey(value));

        if (key == null) {
            if (isVerbose) logger.error("{} is not a valid potion.", value);

            return null;
        }

        return key;
    }

    public static @Nullable PotionEffectType getPotionEffect(@NotNull final String value) {
        if (value.isEmpty()) {
            if (isVerbose) logger.error("{} cannot be blank when fetching the potion effect.", value);

            return null;
        }

        @Nullable final PotionEffectType key = getRegistryAccess().getRegistry(RegistryKey.MOB_EFFECT).get(getKey(value));

        if (key == null) {
            if (isVerbose) logger.error("{} is not a valid potion effect.", value);

            return null;
        }

        return key;
    }

    public static @Nullable Particle getParticleType(@NotNull final String value) {
        if (value.isEmpty()) {
            if (isVerbose) logger.error("{} cannot be blank when fetching the particle.", value);

            return null;
        }

        @Nullable final Particle key = getRegistryAccess().getRegistry(RegistryKey.PARTICLE_TYPE).get(getKey(value));

        if (key == null) {
            if (isVerbose) logger.error("{} is not a valid particle.", value);

            return null;
        }

        return key;
    }

    public static @Nullable PatternType getPatternType(@NotNull final String value) {
        if (value.isEmpty()) {
            if (isVerbose) logger.error("{} cannot be blank when fetching banner pattern types!", value);

            return null;
        }

        @Nullable final PatternType key = getRegistryAccess().getRegistry(RegistryKey.BANNER_PATTERN).get(getKey(value));

        if (key == null) {
            if (isVerbose) logger.error("{} is not a valid banner pattern.", value);

            return null;
        }

        return key;
    }

    public static @Nullable EntityType getEntity(@NotNull final String value) {
        if (value.isEmpty()) {
            if (isVerbose) logger.error("{} cannot be blank when fetching the entity.", value);

            return null;
        }

        @Nullable final EntityType key = getRegistryAccess().getRegistry(RegistryKey.ENTITY_TYPE).get(getKey(value));

        if (key == null) {
            if (isVerbose) logger.error("{} is not a valid entity.", value);

            return null;
        }

        return key;
    }

    public static @Nullable Attribute getAttribute(@NotNull final String value) {
        if (value.isEmpty()) {
            if (isVerbose) logger.error("{} cannot be blank when fetching the attribute.", value);

            return null;
        }

        @Nullable final Attribute key = getRegistryAccess().getRegistry(RegistryKey.ATTRIBUTE).get(getKey(value));

        if (key == null) {
            if (isVerbose) logger.error("{} is not a valid attribute.", value);

            return null;
        }

        return key;
    }

    private static @NotNull NamespacedKey getKey(@NotNull final String value) {
        return NamespacedKey.minecraft(value);
    }

    public static byte[] toBytes(@NotNull final ItemStack itemStack) {
        return itemStack.serializeAsBytes();
    }

    public static @NotNull ItemStack fromBytes(final byte @NotNull [] bytes) {
        return ItemStack.deserializeBytes(bytes);
    }

    public static String toBase64(@NotNull final ItemStack itemStack) {
        return Base64.getEncoder().encodeToString(itemStack.serializeAsBytes());
    }

    public static @NotNull ItemStack fromBase64(@NotNull final String base64) {
        return ItemStack.deserializeBytes(Base64.getDecoder().decode(base64));
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

        int red = Integer.parseInt(rgb[0]);
        int green = Integer.parseInt(rgb[1]);
        int blue = Integer.parseInt(rgb[2]);

        return Color.fromRGB(red, green, blue);
    }
}