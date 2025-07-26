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
import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.th0rgal.oraxen.api.OraxenItems;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class BaseItemBuilder extends IBaseItemBuilder<BaseItemBuilder, ItemStack, ItemType> {

    private final FusionPaper fusion = FusionProvider.getInstance();

    private DataComponentType.Valued<Component> type = DataComponentTypes.ITEM_NAME;
    private Map<String, String> placeholders = new HashMap<>();
    private List<String> displayLore = new ArrayList<>();
    private String displayName = "";

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

    public BaseItemBuilder(@NotNull final ItemStack itemStack) {
        super(itemStack);

        this.itemType = itemStack.getType().asItemType();
    }

    public BaseItemBuilder(@NotNull final String itemStack) {
        super(itemStack);
    }

    public abstract void build();

    public @NotNull ItemStack asItemStack(@NotNull final Audience audience) {
        if (!this.displayName.isEmpty()) {
            this.itemStack.setData(this.type, this.fusion.parse(audience, this.displayName, this.placeholders));
        }

        final List<String> lore = this.displayLore;

        if (!lore.isEmpty()) {
            final List<Component> components = new ArrayList<>(lore.size());

            lore.forEach(line -> components.add(this.fusion.parse(audience, line, placeholders)));

            this.itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(components));
        }

        build();

        return this.itemStack;
    }
    
    public @NotNull ItemStack asItemStack() {
        return asItemStack(Audience.empty());
    }

    public BaseItemBuilder withDisplayName(@NotNull final String displayName, @NotNull final DataComponentType.Valued<Component> type) {
        this.displayName = displayName;
        this.type = type;

        return this;
    }

    public BaseItemBuilder withDisplayName(@NotNull final String displayName) {
        return withDisplayName(displayName, DataComponentTypes.ITEM_NAME);
    }

    public BaseItemBuilder withDisplayLore(@NotNull final List<String> displayLore) {
        this.displayLore = displayLore;

        return this;
    }

    public BaseItemBuilder addDisplayLore(@NotNull final String displayLore) {
        if (displayLore.isEmpty()) return this;

        this.displayLore.add(displayLore);

        return this;
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

    private void getItemsAdder(@NotNull final String itemStack) {
        if (!CustomStack.isInRegistry(itemStack)) {
            this.fusion.log("warn", "The id " + itemStack + " does not exist as an ItemsAdder item!");

            return;
        }

        final CustomStack builder = CustomStack.getInstance(itemStack);

        if (builder == null) {
            throw new FusionException("The id " + itemStack + " is not a valid ItemsAdder item!");
        }

        this.itemStack = builder.getItemStack();
        this.itemType = this.itemStack.getType().asItemType();
    }

    private void getOraxen(@NotNull final String itemStack) {
        if (!OraxenItems.exists(itemStack)) {
            this.fusion.log("warn", "The id " + itemStack + " does not exist as an Oraxen item!");

            return;
        }

        final io.th0rgal.oraxen.items.ItemBuilder builder = OraxenItems.getItemById(itemStack);

        if (builder == null) {
            throw new FusionException("The id " + itemStack + " is not a valid Oraxen item!");
        }

        this.itemStack = builder.build();
        this.itemType = this.itemStack.getType().asItemType();
    }

    private void getNexo(@NotNull final String itemStack) {
        if (!NexoItems.exists(itemStack)) {
            this.fusion.log("warn", "The id " + itemStack + " does not exist as a Nexo item!");

            return;
        }

        final ItemBuilder builder = NexoItems.itemFromId(itemStack);

        if (builder == null) {
            throw new FusionException("The id " + itemStack + " is not a valid Nexo item!");
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