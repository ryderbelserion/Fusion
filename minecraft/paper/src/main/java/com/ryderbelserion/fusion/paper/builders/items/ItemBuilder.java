package com.ryderbelserion.fusion.paper.builders.items;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jspecify.annotations.NullMarked;
import java.util.function.Consumer;

@NullMarked
public class ItemBuilder extends BaseItemBuilder<ItemBuilder> {

    public ItemBuilder(final ItemType itemType, final int amount, final Consumer<BaseItemBuilder> consumer) {
        super(itemType, amount, consumer);
    }

    public ItemBuilder(final ItemType itemType, final Consumer<BaseItemBuilder> consumer) {
        super(itemType, consumer);
    }

    public ItemBuilder(final ItemType itemType, final int amount) {
        super(itemType, amount, consumer -> {});
    }

    public ItemBuilder(final ItemType itemType) {
        this(itemType, 1);
    }

    public ItemBuilder(final ItemStack itemStack) {
        super(itemStack);
    }

    public ItemBuilder(final String itemStack) {
        super(itemStack);
    }

    public static ItemBuilder from(final ItemType itemType, final int amount, final Consumer<BaseItemBuilder> consumer) {
        return new ItemBuilder(itemType, amount, consumer);
    }

    public static ItemBuilder from(final ItemType itemType, final Consumer<BaseItemBuilder> consumer) {
        return from(itemType, 1, consumer);
    }

    public static ItemBuilder from(final ItemType itemType, final int amount) {
        return from(itemType, amount, consumer -> {});
    }

    public static ItemBuilder from(final ItemType itemType) {
        return from(itemType, 1, consumer -> {});
    }

    public static ItemBuilder from(final String itemType, final int amount) {
        return new ItemBuilder(itemType).setAmount(amount);
    }

    public static ItemBuilder from(final String itemType) {
        return from(itemType, 1);
    }

    public static ItemBuilder from(final ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }
}