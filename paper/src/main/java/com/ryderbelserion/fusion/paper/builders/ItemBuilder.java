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
}