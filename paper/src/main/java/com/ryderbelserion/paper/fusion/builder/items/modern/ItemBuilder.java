package com.ryderbelserion.paper.fusion.builder.items.modern;

import com.ryderbelserion.paper.fusion.builder.items.modern.types.PotionBuilder;
import com.ryderbelserion.paper.fusion.builder.items.modern.types.PatternBuilder;
import com.ryderbelserion.paper.fusion.builder.items.modern.types.SkullBuilder;
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

    public static PotionBuilder potion(@NotNull final ItemType itemType, final int amount) {
        return new PotionBuilder(itemType.createItemStack(Math.max(amount, 1)));
    }

    public static PotionBuilder potion(@NotNull final ItemType itemType) {
        return potion(itemType, 1);
    }

    public static SkullBuilder skull(@NotNull final ItemType itemType, final int amount) {
        return new SkullBuilder(itemType.createItemStack(Math.max(amount, 1)));
    }

    public static SkullBuilder skull(@NotNull final ItemType itemType) {
        return skull(itemType, 1);
    }

    public static PatternBuilder pattern(@NotNull final ItemType itemType, final int amount) {
        return new PatternBuilder(itemType.createItemStack(Math.max(amount, 1)));
    }

    public static PatternBuilder pattern(@NotNull final ItemType itemType) {
        return pattern(itemType, 1);
    }

    public static ItemBuilder from(@NotNull final ItemType itemType, final int amount) {
        return new ItemBuilder(itemType.createItemStack(Math.max(amount, 1)));
    }

    public static ItemBuilder from(@NotNull final ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

    public static ItemBuilder from(@NotNull final ItemType itemType) {
        return from(itemType, 1);
    }

    public static ItemBuilder from(@NotNull final String value) {
        return new ItemBuilder(value);
    }
}