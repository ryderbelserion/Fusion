package com.ryderbelserion.vital.paper.api.builders.items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * Handles pdc on item stacks.
 *
 * @author ryderbelserion
 * @version 0.0.2
 * @since 0.0.1
 */
@SuppressWarnings("UnusedReturnValue")
public class NbtBuilder {

    private ItemStack itemStack;

    /**
     * Constructs a new {@link ItemStack} with a dummy {@link Material}.
     *
     * @since 0.0.1
     */
    public NbtBuilder() {
        this(Material.STONE, 1);
    }

    /**
     * Constructs a new {@link ItemStack} with the specified {@link Material}.
     *
     * @param material the {@link Material} to use
     * @since 0.0.1
     */
    public NbtBuilder(@NotNull final Material material) {
        this(material, 1);
    }

    /**
     * Constructs a new {@link ItemStack} with the specified {@link Material} and amount.
     *
     * @param material the {@link Material} to use
     * @param amount the amount to set
     * @since 0.0.1
     */
    public NbtBuilder(@NotNull final Material material, final int amount) {
        this(ItemStack.of(material, amount), true);
    }

    /**
     * Constructs a new {@link ItemStack} with the specified {@link Material} and amount.
     *
     * @param itemStack the {@link ItemStack}
     * @param createNewStack create a new item stack or reuse the passed object.
     * @since 0.0.1
     */
    public NbtBuilder(@NotNull final ItemStack itemStack, final boolean createNewStack) {
        this.itemStack = createNewStack ? itemStack.clone() : itemStack;
    }

    /**
     * Checks if the {@link ItemStack} has {@link ItemMeta}.
     *
     * @return true or false
     * @since 0.0.1
     */
    public final boolean hasItemMeta() {
        return !this.itemStack.hasItemMeta();
    }

    /**
     * Sets the {@link ItemStack}
     *
     * @param itemStack {@link ItemStack}
     * @return {@link NbtBuilder}
     * @since 0.0.1
     */
    public @NotNull final NbtBuilder setItemStack(final ItemStack itemStack) {
        this.itemStack = itemStack;

        return this;
    }

    /**
     * Adds a {@link Double} to the {@link ItemStack} {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey}
     * @param value the {@link Double} to set
     * @return {@link ItemBuilder}
     * @since 0.0.1
     */
    public @NotNull final NbtBuilder setPersistentDouble(@NotNull final NamespacedKey key, final double value) {
        // This must always apply the item meta as we are adding to the storage so item meta will be created anyway.
        this.itemStack.editMeta(itemMeta -> itemMeta.getPersistentDataContainer().set(
                key,
                PersistentDataType.DOUBLE,
                value
        ));

        return this;
    }

    /**
     * Adds a {@link Integer} to the {@link ItemStack} {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey}
     * @param value the {@link Integer} to set
     * @return {@link ItemBuilder}
     * @since 0.0.1
     */
    public @NotNull final NbtBuilder setPersistentInteger(@NotNull final NamespacedKey key, final int value) {
        // This must always apply the item meta as we are adding to the storage so item meta will be created anyway.
        this.itemStack.editMeta(itemMeta -> itemMeta.getPersistentDataContainer().set(
                key,
                PersistentDataType.INTEGER,
                value
        ));

        return this;
    }

    /**
     * Adds a {@link Boolean} to the {@link ItemStack} {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey}
     * @param value the {@link Boolean} to set
     * @return {@link ItemBuilder}
     * @since 0.0.1
     */
    public @NotNull final NbtBuilder setPersistentBoolean(@NotNull final NamespacedKey key, final boolean value) {
        // This must always apply the item meta as we are adding to the storage so item meta will be created anyway.
        this.itemStack.editMeta(itemMeta -> itemMeta.getPersistentDataContainer().set(
                key,
                PersistentDataType.BOOLEAN,
                value
        ));

        return this;
    }

    /**
     * Adds a {@link String} to the {@link ItemStack} {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey}
     * @param value the {@link String} to set
     * @return {@link ItemBuilder}
     * @since 0.0.1
     */
    public @NotNull final NbtBuilder setPersistentString(@NotNull final NamespacedKey key, @NotNull final String value) {
        // This must always apply the item meta as we are adding to the storage so item meta will be created anyway.
        this.itemStack.editMeta(itemMeta -> itemMeta.getPersistentDataContainer().set(
                key,
                PersistentDataType.STRING,
                value
        ));

        return this;
    }

    /**
     * Adds a {@link List<String>} to the {@link ItemStack} {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey}
     * @param values the {@link List<String>} to set
     * @return {@link ItemBuilder}
     * @since 0.0.1
     */
    public @NotNull final NbtBuilder setPersistentList(@NotNull final NamespacedKey key, @NotNull final List<String> values) {
        // This must always apply the item meta as we are adding to the storage so item meta will be created anyway.
        this.itemStack.editMeta(itemMeta -> itemMeta.getPersistentDataContainer().set(
                key,
                PersistentDataType.LIST.listTypeFrom(PersistentDataType.STRING),
                values
        ));

        return this;
    }

    /**
     * Gets a {@link Boolean} from the {@link ItemStack} {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey}
     * @return {@link ItemBuilder}
     * @since 0.0.1
     */
    public final boolean getBoolean(@NotNull final NamespacedKey key) {
        return this.itemStack.getPersistentDataContainer().getOrDefault(key, PersistentDataType.BOOLEAN, false);
    }

    /**
     * Gets a {@link Double} from the {@link ItemStack} {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey}
     * @return {@link ItemBuilder}
     * @since 0.0.1
     */
    public final double getDouble(@NotNull final NamespacedKey key) {
        return this.itemStack.getPersistentDataContainer().getOrDefault(key, PersistentDataType.DOUBLE, 0.0);
    }

    /**
     * Gets a {@link Integer} from the {@link ItemStack} {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey}
     * @return {@link ItemBuilder}
     * @since 0.0.1
     */
    public final int getInteger(@NotNull final NamespacedKey key) {
        return this.itemStack.getPersistentDataContainer().getOrDefault(key, PersistentDataType.INTEGER, 0);
    }

    /**
     * Gets a {@link List<String>} from the {@link ItemStack} {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey}
     * @return {@link ItemBuilder}
     * @since 0.0.1
     */
    public @NotNull final List<String> getList(@NotNull final NamespacedKey key) {
        return this.itemStack.getPersistentDataContainer().getOrDefault(key, PersistentDataType.LIST.strings(), Collections.emptyList());
    }

    /**
     * Gets a {@link String} from the {@link ItemStack} {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey}
     * @return {@link ItemBuilder}
     * @since 0.0.1
     */
    public @NotNull final String getString(@NotNull final NamespacedKey key) {
        return this.itemStack.getPersistentDataContainer().getOrDefault(key, PersistentDataType.STRING, "");
    }

    /**
     * Removes {@link NamespacedKey} from the {@link ItemStack} {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey}
     * @return {@link ItemBuilder}
     * @since 0.0.1
     */
    public @NotNull final NbtBuilder removePersistentKey(@Nullable final NamespacedKey key) {
        if (key == null) return this;
        if (this.hasItemMeta()) return this;

        this.itemStack.editMeta(itemMeta -> itemMeta.getPersistentDataContainer().remove(key));

        return this;
    }

    /**
     * Checks if the {@link ItemStack} has a specific {@link NamespacedKey}.
     *
     * @param key the {@link NamespacedKey}
     * @return {@link ItemBuilder}
     * @since 0.0.1
     */
    public final boolean hasKey(@NotNull final NamespacedKey key) {
        return this.itemStack.getPersistentDataContainer().has(key);
    }

    /**
     * Returns the {@link ItemStack}.
     * 
     * @return {@link ItemStack}
     * @since 0.0.1
     */
    public final ItemStack getItemStack() {
        return this.itemStack;
    }
}