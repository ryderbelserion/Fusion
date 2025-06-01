package com.ryderbelserion.fusion.paper.utils;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.interfaces.ILogger;
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
import java.util.Optional;

public final class ItemUtils {

    private static final FusionCore fusion = FusionCore.Provider.get();

    private static final ILogger logger = fusion.getLogger();

    public static @NotNull RegistryAccess getRegistryAccess() {
        return RegistryAccess.registryAccess();
    }

    public static @NotNull Optional<DataComponentType> getDataComponentType(@NotNull final String value) {
        if (value.isEmpty()) {
            logger.error("{} cannot be blank while fetching a data component.", value);

            return Optional.empty();
        }

        final DataComponentType type = getRegistryAccess().getRegistry(RegistryKey.DATA_COMPONENT_TYPE).get(getKey(value));

        return Optional.ofNullable(type);
    }

    public static @Nullable ItemType getItemType(@NotNull final String value) {
        if (value.isEmpty()) {
            logger.error("{} cannot be blank when fetching item types.", value);

            return null;
        }

        @Nullable final ItemType key = getRegistryAccess().getRegistry(RegistryKey.ITEM).get(getKey(value));

        if (key == null) {
            logger.error("{} is not a valid item type.", value);

            return null;
        }

        return key;
    }

    public static @Nullable Sound getSound(@NotNull final String value) {
        if (value.isEmpty()) {
            logger.error("{} cannot be blank when fetching the sound.", value);

            return null;
        }

        @Nullable final Sound key = getRegistryAccess().getRegistry(RegistryKey.SOUND_EVENT).get(getKey(value));

        if (key == null) {
            logger.error("{} is not a valid sound.", value);

            return null;
        }

        return key;
    }

    public static @Nullable Enchantment getEnchantment(@NotNull final String value) {
        if (value.isEmpty()) {
            logger.error("{} cannot be blank when fetching the enchantment.", value);

            return null;
        }

        @Nullable final Enchantment key = getRegistryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(getKey(value));

        if (key == null) {
            logger.error("{} is not a valid enchantment.", value);

            return null;
        }

        return key;
    }

    public static @Nullable TrimPattern getTrimPattern(@NotNull final String value) {
        if (value.isEmpty()) {
            logger.error("{} cannot be blank when fetching the trim pattern.", value);

            return null;
        }

        @Nullable final TrimPattern key = getRegistryAccess().getRegistry(RegistryKey.TRIM_PATTERN).get(getKey(value));

        if (key == null) {
            logger.error("{} is not a valid trim pattern.", value);

            return null;
        }

        return key;
    }

    public static @Nullable TrimMaterial getTrimMaterial(@NotNull final String value) {
        if (value.isEmpty()) {
            logger.error("{} cannot be blank when fetching the trim material.", value);

            return null;
        }

        @Nullable final TrimMaterial key = getRegistryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(getKey(value));

        if (key == null) {
            logger.error("{} is not a valid trim material.", value);

            return null;
        }

        return key;
    }

    public static @Nullable PotionType getPotionType(@NotNull final String value) {
        if (value.isEmpty()) {
            logger.error("{} cannot be blank when fetching the potion.", value);

            return null;
        }

        @Nullable final PotionType key = getRegistryAccess().getRegistry(RegistryKey.POTION).get(getKey(value));

        if (key == null) {
            logger.error("{} is not a valid potion.", value);

            return null;
        }

        return key;
    }

    public static @Nullable PotionEffectType getPotionEffect(@NotNull final String value) {
        if (value.isEmpty()) {
            logger.error("{} cannot be blank when fetching the potion effect.", value);

            return null;
        }

        @Nullable final PotionEffectType key = getRegistryAccess().getRegistry(RegistryKey.MOB_EFFECT).get(getKey(value));

        if (key == null) {
            logger.error("{} is not a valid potion effect.", value);

            return null;
        }

        return key;
    }

    public static @Nullable Particle getParticleType(@NotNull final String value) {
        if (value.isEmpty()) {
            logger.error("{} cannot be blank when fetching the particle.", value);

            return null;
        }

        @Nullable final Particle key = getRegistryAccess().getRegistry(RegistryKey.PARTICLE_TYPE).get(getKey(value));

        if (key == null) {
            logger.error("{} is not a valid particle.", value);

            return null;
        }

        return key;
    }

    public static @Nullable PatternType getPatternType(@NotNull final String value) {
        if (value.isEmpty()) {
            logger.error("{} cannot be blank when fetching banner pattern types!", value);

            return null;
        }

        @Nullable final PatternType key = getRegistryAccess().getRegistry(RegistryKey.BANNER_PATTERN).get(getKey(value));

        if (key == null) {
            logger.error("{} is not a valid banner pattern.", value);

            return null;
        }

        return key;
    }

    public static @Nullable EntityType getEntity(@NotNull final String value) {
        if (value.isEmpty()) {
            logger.error("{} cannot be blank when fetching the entity.", value);

            return null;
        }

        @Nullable final EntityType key = getRegistryAccess().getRegistry(RegistryKey.ENTITY_TYPE).get(getKey(value));

        if (key == null) {
            logger.error("{} is not a valid entity.", value);

            return null;
        }

        return key;
    }

    public static @Nullable Attribute getAttribute(@NotNull final String value) {
        if (value.isEmpty()) {
            logger.error("{} cannot be blank when fetching the attribute.", value);

            return null;
        }

        @Nullable final Attribute key = getRegistryAccess().getRegistry(RegistryKey.ATTRIBUTE).get(getKey(value));

        if (key == null) {
            logger.error("{} is not a valid attribute.", value);

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

    public static @NotNull String toBase64(@NotNull final ItemStack itemStack) {
        return Base64.getEncoder().encodeToString(itemStack.serializeAsBytes());
    }

    public static @NotNull ItemStack fromBase64(@NotNull final String base64) {
        return ItemStack.deserializeBytes(Base64.getDecoder().decode(base64));
    }
}