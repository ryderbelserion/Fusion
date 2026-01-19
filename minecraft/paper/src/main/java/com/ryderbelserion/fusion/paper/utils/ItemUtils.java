package com.ryderbelserion.fusion.paper.utils;

import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.core.api.enums.Level;
import com.ryderbelserion.fusion.paper.FusionPaper;
import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

public class ItemUtils {

    private static final FusionPaper fusion = (FusionPaper) FusionProvider.getInstance();

    public static @NotNull RegistryAccess getRegistryAccess() {
        return RegistryAccess.registryAccess();
    }

    public static @NotNull Optional<DataComponentType> getDataComponentType(@NotNull final String value) {
        if (value.isEmpty()) {
            fusion.log(Level.ERROR, "{} cannot be blank while fetching a data component.", Map.of("{}", value));

            return Optional.empty();
        }

        final DataComponentType type = getRegistryAccess().getRegistry(RegistryKey.DATA_COMPONENT_TYPE).get(getKey(value));

        return Optional.ofNullable(type);
    }

    public static @Nullable ItemType getItemType(@NotNull final String value) {
        if (value.isEmpty()) {
            fusion.log(Level.ERROR, "{} cannot be blank when fetching item types.", Map.of("{}", value));

            return null;
        }

        // this checks if colon is included, colon represents a namespace.
        // if the colon is not found, we default to the minecraft namespace.
        @Nullable final NamespacedKey key = value.contains(":") ? NamespacedKey.fromString(value) : getKey(value);

        if (key == null) {
            fusion.log(Level.ERROR, "{} is not a valid item key.", Map.of("{}", value));

            return null;
        }

        @Nullable final ItemType itemType = getRegistryAccess().getRegistry(RegistryKey.ITEM).get(getKey(value));

        if (itemType == null) {
            fusion.log(Level.ERROR, "{} is not a valid item type.", Map.of("{}", key.asString()));

            return null;
        }

        return itemType;
    }

    public static @Nullable Sound getSound(@NotNull final String value) {
        if (value.isEmpty()) {
            fusion.log(Level.ERROR, "{} cannot be blank when fetching the sound.", Map.of("{}", value));

            return null;
        }

        // this checks if colon is included, colon represents a namespace.
        // if the colon is not found, we default to the minecraft namespace.
        @Nullable final NamespacedKey key = value.contains(":") ? NamespacedKey.fromString(value) : getKey(value);

        if (key == null) {
            fusion.log(Level.ERROR, "{} is not a valid sound key.", Map.of("{}", value));

            return null;
        }

        @Nullable final Sound sound = getRegistryAccess().getRegistry(RegistryKey.SOUND_EVENT).get(getKey(value));

        if (sound == null) {
            fusion.log(Level.ERROR, "{} is not a valid sound.", Map.of("{}", key.asString()));

            return null;
        }

        return sound;
    }

    public static @Nullable Enchantment getEnchantment(@NotNull final String value) {
        if (value.isEmpty()) {
            fusion.log(Level.ERROR, "{} cannot be blank when fetching the enchantment.", Map.of("{}", value));

            return null;
        }

        // this checks if colon is included, colon represents a namespace.
        // if the colon is not found, we default to the minecraft namespace.
        @Nullable final NamespacedKey key = value.contains(":") ? NamespacedKey.fromString(value) : getKey(value);

        if (key == null) {
            fusion.log(Level.ERROR, "{} is not a valid enchantment key.", Map.of("{}", value));

            return null;
        }

        @Nullable final Enchantment enchantment = getRegistryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(key);

        if (enchantment == null) {
            fusion.log(Level.ERROR, "{} is not a valid enchantment.", Map.of("{}", key.asString()));

            return null;
        }

        return enchantment;
    }

    public static @Nullable TrimPattern getTrimPattern(@NotNull final String value) {
        if (value.isEmpty()) {
            fusion.log(Level.ERROR, "{} cannot be blank when fetching the trim pattern.", Map.of("{}", value));

            return null;
        }

        // this checks if colon is included, colon represents a namespace.
        // if the colon is not found, we default to the minecraft namespace.
        @Nullable final NamespacedKey key = value.contains(":") ? NamespacedKey.fromString(value) : getKey(value);

        if (key == null) {
            fusion.log(Level.ERROR, "{} is not a valid trim pattern key.", Map.of("{}", value));

            return null;
        }

        @Nullable final TrimPattern trimPattern = getRegistryAccess().getRegistry(RegistryKey.TRIM_PATTERN).get(getKey(value));

        if (trimPattern == null) {
            fusion.log(Level.ERROR, "{} is not a valid trim pattern.", Map.of("{}", key.asString()));

            return null;
        }

        return trimPattern;
    }

    public static @Nullable TrimMaterial getTrimMaterial(@NotNull final String value) {
        if (value.isEmpty()) {
            fusion.log(Level.ERROR, "{} cannot be blank when fetching the trim material.", Map.of("{}", value));

            return null;
        }

        // this checks if colon is included, colon represents a namespace.
        // if the colon is not found, we default to the minecraft namespace.
        @Nullable final NamespacedKey key = value.contains(":") ? NamespacedKey.fromString(value) : getKey(value);

        if (key == null) {
            fusion.log(Level.ERROR, "{} is not a valid trim material key.", Map.of("{}", value));

            return null;
        }

        @Nullable final TrimMaterial trimMaterial = getRegistryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(getKey(value));

        if (trimMaterial == null) {
            fusion.log(Level.ERROR, "{} is not a valid trim material.", Map.of("{}", key.asString()));

            return null;
        }

        return trimMaterial;
    }

    public static @Nullable PotionType getPotionType(@NotNull final String value) {
        if (value.isEmpty()) {
            fusion.log(Level.ERROR, "{} cannot be blank when fetching the potion.", Map.of("{}", value));

            return null;
        }

        // this checks if colon is included, colon represents a namespace.
        // if the colon is not found, we default to the minecraft namespace.
        @Nullable final NamespacedKey key = value.contains(":") ? NamespacedKey.fromString(value) : getKey(value);

        if (key == null) {
            fusion.log(Level.ERROR, "{} is not a valid potion type key.", Map.of("{}", value));

            return null;
        }

        @Nullable final PotionType potionType = getRegistryAccess().getRegistry(RegistryKey.POTION).get(getKey(value));

        if (potionType == null) {
            fusion.log(Level.ERROR, "{} is not a valid potion.", Map.of("{}", key.asString()));

            return null;
        }

        return potionType;
    }

    public static @Nullable PotionEffectType getPotionEffect(@NotNull final String value) {
        if (value.isEmpty()) {
            fusion.log(Level.ERROR, "{} cannot be blank when fetching the potion effect.", Map.of("{}", value));

            return null;
        }

        // this checks if colon is included, colon represents a namespace.
        // if the colon is not found, we default to the minecraft namespace.
        @Nullable final NamespacedKey key = value.contains(":") ? NamespacedKey.fromString(value) : getKey(value);

        if (key == null) {
            fusion.log(Level.ERROR, "{} is not a valid potion effect key.", Map.of("{}", value));

            return null;
        }

        @Nullable final PotionEffectType potionEffectType = getRegistryAccess().getRegistry(RegistryKey.MOB_EFFECT).get(getKey(value));

        if (potionEffectType == null) {
            fusion.log(Level.ERROR, "{} is not a valid potion effect.", Map.of("{}", key.asString()));

            return null;
        }

        return potionEffectType;
    }

    public static @Nullable Particle getParticleType(@NotNull final String value) {
        if (value.isEmpty()) {
            fusion.log(Level.ERROR, "{} cannot be blank when fetching the particle.", Map.of("{}", value));

            return null;
        }

        // this checks if colon is included, colon represents a namespace.
        // if the colon is not found, we default to the minecraft namespace.
        @Nullable final NamespacedKey key = value.contains(":") ? NamespacedKey.fromString(value) : getKey(value);

        if (key == null) {
            fusion.log(Level.ERROR, "{} is not a valid potion type key.", Map.of("{}", value));

            return null;
        }

        @Nullable final Particle particle = getRegistryAccess().getRegistry(RegistryKey.PARTICLE_TYPE).get(getKey(value));

        if (particle == null) {
            fusion.log(Level.ERROR, "{} is not a valid particle.", Map.of("{}", key.asString()));

            return null;
        }

        return particle;
    }

    public static @Nullable PatternType getPatternType(@NotNull final String value) {
        if (value.isEmpty()) {
            fusion.log(Level.ERROR, "{} cannot be blank when fetching banner pattern types!", Map.of("{}", value));

            return null;
        }

        // this checks if colon is included, colon represents a namespace.
        // if the colon is not found, we default to the minecraft namespace.
        @Nullable final NamespacedKey key = value.contains(":") ? NamespacedKey.fromString(value) : getKey(value);

        if (key == null) {
            fusion.log(Level.ERROR, "{} is not a valid potion type key.", Map.of("{}", value));

            return null;
        }

        @Nullable final PatternType patternType = getRegistryAccess().getRegistry(RegistryKey.BANNER_PATTERN).get(getKey(value));

        if (patternType == null) {
            fusion.log(Level.ERROR, "{} is not a valid banner pattern.", Map.of("{}", key.asString()));

            return null;
        }

        return patternType;
    }

    public static @Nullable EntityType getEntity(@NotNull final String value) {
        if (value.isEmpty()) {
            fusion.log(Level.ERROR, "{} cannot be blank when fetching the entity.", Map.of("{}", value));

            return null;
        }

        // this checks if colon is included, colon represents a namespace.
        // if the colon is not found, we default to the minecraft namespace.
        @Nullable final NamespacedKey key = value.contains(":") ? NamespacedKey.fromString(value) : getKey(value);

        if (key == null) {
            fusion.log(Level.ERROR, "{} is not a valid potion type key.", Map.of("{}", value));

            return null;
        }

        @Nullable final EntityType entityType = getRegistryAccess().getRegistry(RegistryKey.ENTITY_TYPE).get(getKey(value));

        if (entityType == null) {
            fusion.log(Level.ERROR, "{} is not a valid entity.", Map.of("{}", key.asString()));

            return null;
        }

        return entityType;
    }

    public static @Nullable Attribute getAttribute(@NotNull final String value) {
        if (value.isEmpty()) {
            fusion.log(Level.ERROR, "{} cannot be blank when fetching the attribute.", Map.of("{}", value));

            return null;
        }

        // this checks if colon is included, colon represents a namespace.
        // if the colon is not found, we default to the minecraft namespace.
        @Nullable final NamespacedKey key = value.contains(":") ? NamespacedKey.fromString(value) : getKey(value);

        if (key == null) {
            fusion.log(Level.ERROR, "{} is not a valid potion type key.", Map.of("{}", value));

            return null;
        }

        @Nullable final Attribute attribute = getRegistryAccess().getRegistry(RegistryKey.ATTRIBUTE).get(getKey(value));

        if (attribute == null) {
            fusion.log(Level.ERROR, "{} is not a valid attribute.", Map.of("{}", key.asString()));

            return null;
        }

        return attribute;
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

    public static @NotNull String toBase64(@NotNull final ItemStack itemStack) {
        return Base64.getEncoder().encodeToString(itemStack.serializeAsBytes());
    }

    public static @NotNull ItemStack fromBase64(@NotNull final String base64) {
        return ItemStack.deserializeBytes(Base64.getDecoder().decode(base64));
    }
}