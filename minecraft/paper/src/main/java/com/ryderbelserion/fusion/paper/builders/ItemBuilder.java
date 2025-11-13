package com.ryderbelserion.fusion.paper.builders;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;

public class ItemBuilder extends BaseItemBuilder<ItemBuilder> {

    public ItemBuilder(@NotNull final ItemType itemType, final int amount, @NotNull final Consumer<BaseItemBuilder> consumer) {
        super(itemType, amount, consumer);
    }

    public ItemBuilder(@NotNull final ItemType itemType, @NotNull final Consumer<BaseItemBuilder> consumer) {
        super(itemType, consumer);
    }

    public ItemBuilder(@NotNull final ItemType itemType, final int amount) {
        super(itemType, amount, consumer -> {});
    }

    public ItemBuilder(@NotNull final ItemType itemType) {
        this(itemType, 1);
    }

    public ItemBuilder(@NotNull final ItemStack itemStack) {
        super(itemStack);
    }

    public ItemBuilder(@NotNull final String itemStack) {
        super(itemStack);
    }

    public static ItemBuilder from(@NotNull final ItemType itemType, final int amount, @NotNull final Consumer<BaseItemBuilder> consumer) {
        return new ItemBuilder(itemType, amount, consumer);
    }

    public static ItemBuilder from(@NotNull final ItemType itemType, @NotNull final Consumer<BaseItemBuilder> consumer) {
        return from(itemType, 1, consumer);
    }

    public static ItemBuilder from(@NotNull final ItemType itemType, final int amount) {
        return from(itemType, amount, consumer -> {});
    }

    public static ItemBuilder from(@NotNull final ItemType itemType) {
        return from(itemType, 1, consumer -> {});
    }

    public static ItemBuilder from(@NotNull final String itemType, final int amount) {
        return new ItemBuilder(itemType).setAmount(amount);
    }

    public static ItemBuilder from(@NotNull final String itemType) {
        return from(itemType, 1);
    }

    public static ItemBuilder from(@NotNull final ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }
}