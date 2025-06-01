package com.ryderbelserion.fusion.paper.api.builders.items;

import com.ryderbelserion.fusion.paper.api.builders.items.types.PotionBuilder;
import com.ryderbelserion.fusion.paper.api.builders.items.types.PatternBuilder;
import com.ryderbelserion.fusion.paper.api.builders.items.types.SkullBuilder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;

public class ItemBuilder extends BaseItemBuilder<ItemBuilder> {

    ItemBuilder(@NotNull ItemStack item) {
        super(item);
    }

    ItemBuilder(@NotNull String item) {
        super(item);
    }

    public static @NotNull PotionBuilder potion(@NotNull ItemType itemType, int amount) {
        return new PotionBuilder(itemType.createItemStack(Math.max(amount, 1)));
    }

    public static @NotNull PotionBuilder potion(@NotNull ItemType itemType) {
        return potion(itemType, 1);
    }

    public static @NotNull SkullBuilder skull(@NotNull ItemType itemType, int amount) {
        return new SkullBuilder(itemType.createItemStack(Math.max(amount, 1)));
    }

    public static @NotNull SkullBuilder skull(@NotNull ItemType itemType) {
        return skull(itemType, 1);
    }

    public static @NotNull PatternBuilder pattern(@NotNull ItemType itemType, int amount) {
        return new PatternBuilder(itemType.createItemStack(Math.max(amount, 1)));
    }

    public static @NotNull PatternBuilder pattern(@NotNull ItemType itemType) {
        return pattern(itemType, 1);
    }

    public static @NotNull ItemBuilder from(@NotNull ItemType itemType, int amount) {
        return new ItemBuilder(itemType.createItemStack(Math.max(amount, 1)));
    }

    public static @NotNull ItemBuilder from(@NotNull ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

    public static @NotNull ItemBuilder from(@NotNull ItemType itemType) {
        return from(itemType, 1);
    }

    public static @NotNull ItemBuilder from(@NotNull String value) {
        return new ItemBuilder(value);
    }
}