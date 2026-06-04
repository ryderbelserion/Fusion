package com.ryderbelserion.fusion.paper.builders.items.types.plugins.types.custom;

import com.ryderbelserion.fusion.core.api.enums.Level;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.paper.builders.items.BaseItemBuilder;
import com.ryderbelserion.fusion.paper.builders.items.types.plugins.ICustomItem;
import com.ryderbelserion.fusion.paper.builders.items.types.plugins.types.VanillaItemStack;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import java.util.Optional;

public class ItemsAdderCustomItem extends ICustomItem {

    public ItemsAdderCustomItem(@NonNull final BaseItemBuilder builder, @NonNull final String item, final boolean isEnabled) {
        super(builder, item, isEnabled);
    }

    private ItemStack itemStack;

    @Override
    public @NonNull final Optional<ItemStack> getItemStack() {
        return Optional.ofNullable(this.itemStack);
    }

    @Override
    public @NonNull final ItemsAdderCustomItem init() {
        if (!this.isEnabled && !this.fusion.isPluginEnabled("ItemsAdder")) {
            new VanillaItemStack(this.builder, this.item).init();

            return this;
        }

        if (!CustomStack.isInRegistry(this.item)) {
            this.fusion.log(Level.WARNING, "The id %s does not exist as a ItemsAdder item! Attempting falling back to vanilla item!", this.item);

            new VanillaItemStack(this.builder, this.item).init();

            return this;
        }

        final CustomStack builder = CustomStack.getInstance(this.item);

        if (builder == null) {
            throw new FusionException("The id " + this.item + " is not a valid ItemsAdder item!");
        }

        this.itemStack = builder.getItemStack();

        return this;
    }
}