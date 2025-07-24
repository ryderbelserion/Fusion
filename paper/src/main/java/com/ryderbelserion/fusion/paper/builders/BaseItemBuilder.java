package com.ryderbelserion.fusion.paper.builders;

import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.items.ItemBuilder;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.api.interfaces.builders.IBaseItemBuilder;
import com.ryderbelserion.fusion.core.api.support.ModSupport;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.FusionProvider;
import com.ryderbelserion.fusion.paper.utils.ItemUtils;
import dev.lone.itemsadder.api.CustomStack;
import io.th0rgal.oraxen.api.OraxenItems;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;

public class BaseItemBuilder extends IBaseItemBuilder<BaseItemBuilder, ItemStack, ItemType> {

    private final FusionPaper fusion = FusionProvider.getInstance();

    public BaseItemBuilder(@NotNull final ItemType itemType, final int amount, @NotNull final Consumer<BaseItemBuilder> consumer) {
        super(itemType.createItemStack(amount));

        // bind item type.
        this.itemType = itemType;

        // apply extra data to this class.
        consumer.accept(this);
    }

    public BaseItemBuilder(@NotNull final ItemType itemType, @NotNull final Consumer<BaseItemBuilder> consumer) {
        this(itemType, 1, consumer);
    }

    @Override
    public BaseItemBuilder withCustomItem(@NotNull final String itemStack) {
        final String plugin = this.fusion.getConfig().getItemsPlugin();

        switch (plugin.toLowerCase()) {
            case "nexo" -> {
                if (this.fusion.isModReady(ModSupport.nexo)) {
                    getNexo(itemStack);

                    return this;
                }

                setItem(itemStack);
            }

            case "oraxen" -> {
                if (this.fusion.isModReady(ModSupport.oraxen)) {
                    getOraxen(itemStack);

                    return this;
                }

                setItem(itemStack);
            }

            case "itemsadder" -> {
                if (this.fusion.isModReady(ModSupport.items_adder)) {
                    getItemsAdder(itemStack);

                    return this;
                }

                setItem(itemStack);
            }

            case "none" -> setItem(itemStack);

            default -> {
                if (this.fusion.isModReady(ModSupport.nexo)) {
                    getNexo(itemStack);

                    return this;
                }

                if (this.fusion.isModReady(ModSupport.items_adder)) {
                    getItemsAdder(itemStack);

                    return this;
                }

                if (this.fusion.isModReady(ModSupport.oraxen)) {
                    getOraxen(itemStack);

                    return this;
                }

                setItem(itemStack);
            }
        }

        return this;
    }

    @Override
    public BaseItemBuilder withBase64(@NotNull final String itemStack) {
        if (itemStack.isEmpty()) return this;

        try {
            this.itemStack = ItemUtils.fromBase64(itemStack);
        } catch (final Exception exception) {
            final ItemType stone = ItemType.STONE;

            this.itemStack = stone.createItemStack();
            this.itemType = stone;
        }

        this.itemType = this.itemStack.getType().asItemType();

        return this;
    }

    @Override
    public BaseItemBuilder withType(@NotNull final ItemType itemType, final int amount) {
        final ItemStack itemStack = itemType.createItemStack(Math.max(amount, 1));

        this.itemStack = this.itemStack == null ? itemStack : this.itemStack.withType(itemStack.getType());
        this.itemStack.setAmount(itemStack.getAmount());

        this.itemType = itemType;

        return this;
    }

    @Override
    public BaseItemBuilder withType(@NotNull final ItemType itemType) {
        return withType(itemType, 1);
    }

    private void getItemsAdder(@NotNull final String item) {
        if (!CustomStack.isInRegistry(item)) {
            return;
        }

        final CustomStack builder = CustomStack.getInstance(item);

        if (builder == null) {
            throw new FusionException("The id " + item + " is not a valid ItemsAdder item!");
        }

        this.itemStack = builder.getItemStack();
        this.itemType = this.itemStack.getType().asItemType();
    }

    private void getOraxen(@NotNull final String item) {
        if (!OraxenItems.exists(item)) {
            return;
        }

        final io.th0rgal.oraxen.items.ItemBuilder builder = OraxenItems.getItemById(item);

        if (builder == null) {
            throw new FusionException("The id " + item + " is not a valid Oraxen item!");
        }

        this.itemStack = builder.build();
        this.itemType = this.itemStack.getType().asItemType();
    }

    private void getNexo(@NotNull final String item) {
        if (!NexoItems.exists(item)) {
            return;
        }

        final ItemBuilder builder = NexoItems.itemFromId(item);

        if (builder == null) {
            throw new FusionException("The id " + item + " is not a valid Nexo item!");
        }

        this.itemStack = builder.build();
        this.itemType = this.itemStack.getType().asItemType();
    }

    private void setItem(@NotNull final String itemStack) {
        final ItemType itemType = ItemUtils.getItemType(itemStack);

        if (itemType == null) {
            withBase64(itemStack);

            return;
        }

        withType(itemType);
    }
}