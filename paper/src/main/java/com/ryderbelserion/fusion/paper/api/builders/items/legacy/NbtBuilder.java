package com.ryderbelserion.fusion.paper.api.builders.items.legacy;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.List;

public class NbtBuilder {

    private ItemStack itemStack;

    public NbtBuilder() {
        this(ItemType.STONE, 1);
    }

    public NbtBuilder(@NotNull final ItemType itemType) {
        this(itemType, 1);
    }

    public NbtBuilder(@NotNull final ItemType itemType, final int amount) {
        this(itemType.createItemStack(amount), true);
    }

    public NbtBuilder(@NotNull final ItemStack itemStack, final boolean createNewStack) {
        this.itemStack = createNewStack ? itemStack.clone() : itemStack;
    }

    public @NotNull final NbtBuilder setItemStack(@NotNull final ItemStack itemStack) {
        this.itemStack = itemStack;

        return this;
    }

    public @NotNull final NbtBuilder setPersistentDouble(@NotNull final NamespacedKey key, final double value) {
        this.itemStack.editPersistentDataContainer(container -> container.set(key, PersistentDataType.DOUBLE, value));

        return this;
    }

    public @NotNull final NbtBuilder setPersistentInteger(@NotNull final NamespacedKey key, final int value) {
        this.itemStack.editPersistentDataContainer(container -> container.set(key, PersistentDataType.INTEGER, value));

        return this;
    }

    public @NotNull final NbtBuilder setPersistentBoolean(@NotNull final NamespacedKey key, final boolean value) {
        this.itemStack.editPersistentDataContainer(container -> container.set(key, PersistentDataType.BOOLEAN, value));

        return this;
    }

    public @NotNull final NbtBuilder setPersistentString(@NotNull final NamespacedKey key, @NotNull final String value) {
        this.itemStack.editPersistentDataContainer(container -> container.set(key, PersistentDataType.STRING, value));

        return this;
    }

    public @NotNull final NbtBuilder setPersistentList(@NotNull final NamespacedKey key, @NotNull final List<String> values) {
        this.itemStack.editPersistentDataContainer(container -> container.set(key, PersistentDataType.LIST.listTypeFrom(PersistentDataType.STRING), values));

        return this;
    }

    public final boolean getBoolean(@NotNull final NamespacedKey key) {
        return this.itemStack.getPersistentDataContainer().getOrDefault(key, PersistentDataType.BOOLEAN, false);
    }

    public final double getDouble(@NotNull final NamespacedKey key) {
        return this.itemStack.getPersistentDataContainer().getOrDefault(key, PersistentDataType.DOUBLE, 0.0);
    }

    public final int getInteger(@NotNull final NamespacedKey key) {
        return this.itemStack.getPersistentDataContainer().getOrDefault(key, PersistentDataType.INTEGER, 0);
    }

    public @NotNull final List<String> getList(@NotNull final NamespacedKey key) {
        return this.itemStack.getPersistentDataContainer().getOrDefault(key, PersistentDataType.LIST.strings(), Collections.emptyList());
    }

    public @NotNull final String getString(@NotNull final NamespacedKey key) {
        return this.itemStack.getPersistentDataContainer().getOrDefault(key, PersistentDataType.STRING, "");
    }

    public @NotNull final NbtBuilder removePersistentKey(@Nullable final NamespacedKey key) {
        if (key == null) return this;

        this.itemStack.editPersistentDataContainer(container -> {
            if (container.has(key)) container.remove(key);
        });

        return this;
    }

    public final boolean hasKey(@NotNull final NamespacedKey key) {
        return this.itemStack.getPersistentDataContainer().has(key);
    }

    public final ItemStack getItemStack() {
        return this.itemStack;
    }
}