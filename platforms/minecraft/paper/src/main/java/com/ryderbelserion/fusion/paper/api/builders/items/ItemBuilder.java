package com.ryderbelserion.fusion.paper.api.builders.items;

import com.ryderbelserion.fusion.paper.api.builders.items.types.PotionBuilder;
import com.ryderbelserion.fusion.paper.api.builders.items.types.PatternBuilder;
import com.ryderbelserion.fusion.paper.api.builders.items.types.SkullBuilder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;

public class ItemBuilder extends BaseItemBuilder<ItemBuilder> {

    ItemBuilder(@NotNull final ItemStack itemStack) {
        super(itemStack);
    }

    ItemBuilder(@NotNull final String value) {
        super(value);
    }

    public static @NotNull PotionBuilder potion(@NotNull final ItemType itemType, final int amount) {
        return new PotionBuilder(itemType.createItemStack(Math.max(amount, 1)));
    }

    public static @NotNull PotionBuilder potion(@NotNull final ItemType itemType) {
        return potion(itemType, 1);
    }

    public static @NotNull SkullBuilder skull(@NotNull final ItemType itemType, final int amount) {
        return new SkullBuilder(itemType.createItemStack(Math.max(amount, 1)));
    }

    public static @NotNull SkullBuilder skull(@NotNull final ItemType itemType) {
        return skull(itemType, 1);
    }

    public static @NotNull PatternBuilder pattern(@NotNull final ItemType itemType, final int amount) {
        return new PatternBuilder(itemType.createItemStack(Math.max(amount, 1)));
    }

    public static @NotNull PatternBuilder pattern(@NotNull final ItemType itemType) {
        return pattern(itemType, 1);
    }

    public static @NotNull ItemBuilder from(@NotNull final ItemType itemType, final int amount) {
        return new ItemBuilder(itemType.createItemStack(Math.max(amount, 1)));
    }

    public static @NotNull ItemBuilder from(@NotNull final ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

    public static @NotNull ItemBuilder from(@NotNull final ItemType itemType) {
        return from(itemType, 1);
    }

    public static @NotNull ItemBuilder from(@NotNull final String value) {
        return new ItemBuilder(value);
    }
}