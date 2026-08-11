package com.ryderbelserion.fusion.paper.utils;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ryderbelserion.fusion.api.FusionProvider;
import com.ryderbelserion.fusion.api.enums.Level;
import com.ryderbelserion.fusion.paper.FusionPaper;
import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.commands.arguments.item.ItemParser;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.Item;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jspecify.annotations.NullMarked;
import java.util.Base64;
import java.util.Optional;

@NullMarked
public class ItemUtils {

    private static final FusionPaper fusion = (FusionPaper) FusionProvider.api();

    public static RegistryAccess getRegistryAccess() {
        return RegistryAccess.registryAccess();
    }

    public static Optional<ItemStack> getItemStack(final String context) {
        ItemInput parser = null;

        try {
            parser = new ItemParser(CraftRegistry.getMinecraftRegistry()).parse(new StringReader(context));
        } catch (final CommandSyntaxException exception) {
            exception.printStackTrace();
        }

        if (parser == null) {
            return Optional.empty();
        }

        final Item item = parser.item().value();

        final net.minecraft.world.item.ItemStack itemStack = new net.minecraft.world.item.ItemStack(item);

        final DataComponentPatch component = parser.components();

        itemStack.applyComponents(component);

        return Optional.of(CraftItemStack.asCraftMirror(itemStack));
    }

    public static Optional<DataComponentType> getDataComponentType(final String value) {
        if (value.isEmpty()) {
            fusion.log(Level.error, "%s cannot be blank while fetching a data component.", value);

            return Optional.empty();
        }

        final DataComponentType type = getRegistryAccess().getRegistry(RegistryKey.DATA_COMPONENT_TYPE).get(getKey(value));

        return Optional.ofNullable(type);
    }

    public static Optional<ItemType> getItemType(final String value) {
        if (value.isEmpty()) {
            fusion.log(Level.error, "%s cannot be blank when fetching item types.", value);

            return Optional.empty();
        }

        // this checks if colon is included, colon represents a namespace.
        // if the colon is not found, we default to the minecraft namespace.
        final NamespacedKey key = value.contains(":") ? NamespacedKey.fromString(value) : getKey(value);

        if (key == null) {
            fusion.log(Level.error, "%s is not a valid item key.", value);

            return Optional.empty();
        }

        final ItemType itemType = getRegistryAccess().getRegistry(RegistryKey.ITEM).get(getKey(value));

        if (itemType == null) {
            fusion.log(Level.error, "%s is not a valid item type.", key.asString());

            return Optional.empty();
        }

        return Optional.of(itemType);
    }

    public static Optional<Sound> getSound(final String value) {
        if (value.isEmpty()) {
            fusion.log(Level.error, "%s cannot be blank when fetching the sound.", value);

            return Optional.empty();
        }

        // this checks if colon is included, colon represents a namespace.
        // if the colon is not found, we default to the minecraft namespace.
        final NamespacedKey key = value.contains(":") ? NamespacedKey.fromString(value) : getKey(value);

        if (key == null) {
            fusion.log(Level.error, "%s is not a valid sound key.", value);

            return Optional.empty();
        }

        final Sound sound = getRegistryAccess().getRegistry(RegistryKey.SOUND_EVENT).get(getKey(value));

        if (sound == null) {
            fusion.log(Level.error, "%s is not a valid sound.", key.asString());

            return Optional.empty();
        }

        return Optional.of(sound);
    }

    public static Optional<Enchantment> getEnchantment(final String value) {
        if (value.isEmpty()) {
            fusion.log(Level.error, "%s cannot be blank when fetching the enchantment.", value);

            return Optional.empty();
        }

        // this checks if colon is included, colon represents a namespace.
        // if the colon is not found, we default to the minecraft namespace.
        final NamespacedKey key = value.contains(":") ? NamespacedKey.fromString(value) : getKey(value);

        if (key == null) {
            fusion.log(Level.error, "%s is not a valid enchantment key.", value);

            return Optional.empty();
        }

        final Enchantment enchantment = getRegistryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(key);

        if (enchantment == null) {
            fusion.log(Level.error, "%s is not a valid enchantment.", key.asString());

            return Optional.empty();
        }

        return Optional.of(enchantment);
    }

    public static Optional<TrimPattern> getTrimPattern(final String value) {
        if (value.isEmpty()) {
            fusion.log(Level.error, "%s cannot be blank when fetching the trim pattern.", value);

            return Optional.empty();
        }

        // this checks if colon is included, colon represents a namespace.
        // if the colon is not found, we default to the minecraft namespace.
        final NamespacedKey key = value.contains(":") ? NamespacedKey.fromString(value) : getKey(value);

        if (key == null) {
            fusion.log(Level.error, "%s is not a valid trim pattern key.", value);

            return Optional.empty();
        }

        final TrimPattern trimPattern = getRegistryAccess().getRegistry(RegistryKey.TRIM_PATTERN).get(getKey(value));

        if (trimPattern == null) {
            fusion.log(Level.error, "%s is not a valid trim pattern.", key.asString());

            return Optional.empty();
        }

        return Optional.of(trimPattern);
    }

    public static Optional<TrimMaterial> getTrimMaterial(final String value) {
        if (value.isEmpty()) {
            fusion.log(Level.error, "%s cannot be blank when fetching the trim material.", value);

            return Optional.empty();
        }

        // this checks if colon is included, colon represents a namespace.
        // if the colon is not found, we default to the minecraft namespace.
        final NamespacedKey key = value.contains(":") ? NamespacedKey.fromString(value) : getKey(value);

        if (key == null) {
            fusion.log(Level.error, "%s is not a valid trim material key.", value);

            return Optional.empty();
        }

        final TrimMaterial trimMaterial = getRegistryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(getKey(value));

        if (trimMaterial == null) {
            fusion.log(Level.error, "%s is not a valid trim material.", key.asString());

            return Optional.empty();
        }

        return Optional.of(trimMaterial);
    }

    public static Optional<PotionType> getPotionType(final String value) {
        if (value.isEmpty()) {
            fusion.log(Level.error, "%s cannot be blank when fetching the potion.", value);

            return Optional.empty();
        }

        // this checks if colon is included, colon represents a namespace.
        // if the colon is not found, we default to the minecraft namespace.
        final NamespacedKey key = value.contains(":") ? NamespacedKey.fromString(value) : getKey(value);

        if (key == null) {
            fusion.log(Level.error, "%s is not a valid potion type key.", value);

            return Optional.empty();
        }

        final PotionType potionType = getRegistryAccess().getRegistry(RegistryKey.POTION).get(getKey(value));

        if (potionType == null) {
            fusion.log(Level.error, "%s is not a valid potion.", key.asString());

            return Optional.empty();
        }

        return Optional.of(potionType);
    }

    public static Optional<PotionEffectType> getPotionEffect(final String value) {
        if (value.isEmpty()) {
            fusion.log(Level.error, "%s cannot be blank when fetching the potion effect.", value);

            return Optional.empty();
        }

        // this checks if colon is included, colon represents a namespace.
        // if the colon is not found, we default to the minecraft namespace.
        final NamespacedKey key = value.contains(":") ? NamespacedKey.fromString(value) : getKey(value);

        if (key == null) {
            fusion.log(Level.error, "%s is not a valid potion effect key.", value);

            return Optional.empty();
        }

        final PotionEffectType potionEffectType = getRegistryAccess().getRegistry(RegistryKey.MOB_EFFECT).get(getKey(value));

        if (potionEffectType == null) {
            fusion.log(Level.error, "%s is not a valid potion effect.", key.asString());

            return Optional.empty();
        }

        return Optional.of(potionEffectType);
    }

    public static Optional<Particle> getParticleType(final String value) {
        if (value.isEmpty()) {
            fusion.log(Level.error, "%s cannot be blank when fetching the particle.", value);

            return Optional.empty();
        }

        // this checks if colon is included, colon represents a namespace.
        // if the colon is not found, we default to the minecraft namespace.
        final NamespacedKey key = value.contains(":") ? NamespacedKey.fromString(value) : getKey(value);

        if (key == null) {
            fusion.log(Level.error, "%s is not a valid potion type key.", value);

            return Optional.empty();
        }

        final Particle particle = getRegistryAccess().getRegistry(RegistryKey.PARTICLE_TYPE).get(getKey(value));

        if (particle == null) {
            fusion.log(Level.error, "%s is not a valid particle.", key.asString());

            return Optional.empty();
        }

        return Optional.of(particle);
    }

    public static Optional<PatternType> getPatternType(final String value) {
        if (value.isEmpty()) {
            fusion.log(Level.error, "%s cannot be blank when fetching banner pattern types!", value);

            return Optional.empty();
        }

        // this checks if colon is included, colon represents a namespace.
        // if the colon is not found, we default to the minecraft namespace.
        final NamespacedKey key = value.contains(":") ? NamespacedKey.fromString(value) : getKey(value);

        if (key == null) {
            fusion.log(Level.error, "%s is not a valid potion type key.", value);

            return Optional.empty();
        }

        final PatternType patternType = getRegistryAccess().getRegistry(RegistryKey.BANNER_PATTERN).get(getKey(value));

        if (patternType == null) {
            fusion.log(Level.error, "%s is not a valid banner pattern.", key.asString());

            return Optional.empty();
        }

        return Optional.of(patternType);
    }

    public static Optional<EntityType> getEntity(final String value) {
        if (value.isEmpty()) {
            fusion.log(Level.error, "%s cannot be blank when fetching the entity.", value);

            return Optional.empty();
        }

        // this checks if colon is included, colon represents a namespace.
        // if the colon is not found, we default to the minecraft namespace.
        final NamespacedKey key = value.contains(":") ? NamespacedKey.fromString(value) : getKey(value);

        if (key == null) {
            fusion.log(Level.error, "%s is not a valid potion type key.", value);

            return Optional.empty();
        }

        final EntityType entityType = getRegistryAccess().getRegistry(RegistryKey.ENTITY_TYPE).get(getKey(value));

        if (entityType == null) {
            fusion.log(Level.error, "%s is not a valid entity.", key.asString());

            return Optional.empty();
        }

        return Optional.of(entityType);
    }

    public static Optional<Attribute> getAttribute(final String value) {
        if (value.isEmpty()) {
            fusion.log(Level.error, "%s cannot be blank when fetching the attribute.", value);

            return Optional.empty();
        }

        // this checks if colon is included, colon represents a namespace.
        // if the colon is not found, we default to the minecraft namespace.
        final NamespacedKey key = value.contains(":") ? NamespacedKey.fromString(value) : getKey(value);

        if (key == null) {
            fusion.log(Level.error, "%s is not a valid potion type key.", value);

            return Optional.empty();
        }

        final Attribute attribute = getRegistryAccess().getRegistry(RegistryKey.ATTRIBUTE).get(getKey(value));

        if (attribute == null) {
            fusion.log(Level.error, "%s is not a valid attribute.", key.asString());

            return Optional.empty();
        }

        return Optional.of(attribute);
    }

    private static NamespacedKey getKey(final String value) {
        return NamespacedKey.minecraft(value);
    }

    public static byte[] toBytes(final ItemStack itemStack) {
        return itemStack.serializeAsBytes();
    }

    public static ItemStack fromBytes(final byte [] bytes) {
        return ItemStack.deserializeBytes(bytes);
    }

    public static String toBase64(final ItemStack itemStack) {
        return Base64.getEncoder().encodeToString(itemStack.serializeAsBytes());
    }

    public static ItemStack fromBase64(final String base64) {
        return ItemStack.deserializeBytes(Base64.getDecoder().decode(base64));
    }
}