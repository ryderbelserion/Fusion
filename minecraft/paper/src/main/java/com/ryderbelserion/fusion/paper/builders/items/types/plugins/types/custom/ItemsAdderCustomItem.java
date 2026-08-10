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

public final class ItemsAdderCustomItem extends ICustomItem {

    public ItemsAdderCustomItem(@NonNull final BaseItemBuilder builder, @NonNull final String item, final boolean isEnabled) {
        super(builder, item, isEnabled);
    }

    private ItemStack itemStack;

    @Override
    public @NonNull Optional<ItemStack> getItemStack() {
        return Optional.ofNullable(this.itemStack);
    }

    @Override
    public @NonNull ItemsAdderCustomItem init() {
        final String impl = getImpl();

        if (!this.isEnabled && !this.fusion.isModReady(impl)) {
            new VanillaItemStack(this.builder, this.item).init();

            return this;
        }

        if (!isAvailable()) {
            this.fusion.log(Level.WARNING, "The id %s does not exist as a %s item! Attempting falling back to vanilla item!", this.item, impl);

            new VanillaItemStack(this.builder, this.item).init();

            return this;
        }

        final CustomStack builder = CustomStack.getInstance(this.item);

        if (builder == null) {
            throw new FusionException("The id " + this.item + " is not a valid %s item!".formatted(impl));
        }

        this.itemStack = builder.getItemStack();

        return this;
    }

    @Override
    public boolean isAvailable() {
        try {
            return CustomStack.isInRegistry(this.item);
        } catch (final Exception exception) {
            return false;
        }
    }

    @Override
    public @NonNull String getImpl() {
        return "ItemsAdder";
    }
}