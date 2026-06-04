package com.ryderbelserion.fusion.paper.builders.items.types.plugins.types.custom;

import com.ryderbelserion.fusion.core.api.enums.Level;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.paper.builders.items.BaseItemBuilder;
import com.ryderbelserion.fusion.paper.builders.items.types.plugins.ICustomItem;
import com.ryderbelserion.fusion.paper.builders.items.types.plugins.types.VanillaItemStack;
import io.th0rgal.oraxen.api.OraxenItems;
import io.th0rgal.oraxen.items.ItemBuilder;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import java.util.Optional;

public class OraxenCustomItem extends ICustomItem {

    public OraxenCustomItem(@NonNull final BaseItemBuilder builder, @NonNull final String item, final boolean isEnabled) {
        super(builder, item, isEnabled);
    }

    private ItemStack itemStack;

    @Override
    public @NonNull final Optional<ItemStack> getItemStack() {
        return Optional.ofNullable(this.itemStack);
    }

    @Override
    public @NonNull final OraxenCustomItem init() {
        if (!this.isEnabled && !this.fusion.isPluginEnabled("Oraxen")) {
            new VanillaItemStack(this.builder, this.item).init();

            return this;
        }

        if (!OraxenItems.exists(this.item)) {
            this.fusion.log(Level.WARNING, "The id %s does not exist as a Oraxen item! Attempting falling back to vanilla item!", this.item);

            new VanillaItemStack(this.builder, this.item).init();

            return this;
        }

        final ItemBuilder builder = OraxenItems.getItemById(this.item);

        if (builder == null) {
            throw new FusionException("The id " + this.item + " is not a valid Oraxen item!");
        }

        this.itemStack = builder.build();

        return this;
    }
}