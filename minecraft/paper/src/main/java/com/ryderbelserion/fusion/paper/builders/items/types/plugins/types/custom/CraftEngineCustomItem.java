package com.ryderbelserion.fusion.paper.builders.items.types.plugins.types.custom;

import com.ryderbelserion.fusion.api.enums.Level;
import com.ryderbelserion.fusion.paper.builders.items.BaseItemBuilder;
import com.ryderbelserion.fusion.paper.builders.items.types.plugins.ICustomItem;
import com.ryderbelserion.fusion.paper.builders.items.types.plugins.types.VanillaItemStack;
import net.momirealms.craftengine.bukkit.api.CraftEngineItems;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import java.util.Optional;

public final class CraftEngineCustomItem extends ICustomItem {

    public CraftEngineCustomItem(@NonNull final BaseItemBuilder builder, @NonNull final String item, final boolean isEnabled) {
        super(builder, item, isEnabled);
    }

    private ItemStack itemStack;

    @Override
    public @NonNull Optional<ItemStack> getItemStack() {
        return Optional.ofNullable(this.itemStack);
    }

    @Override
    public @NonNull CraftEngineCustomItem init() {
        final String impl = getImpl();

        if (!this.isEnabled && !this.fusion.isModReady(impl)) {
            new VanillaItemStack(this.builder, this.item).init();

            return this;
        }

        Optional.ofNullable(CraftEngineItems.byId(this.item)).ifPresentOrElse(item -> this.itemStack = item.buildBukkitItem(), () -> {
            this.fusion.log(Level.warn, "The id %s does not exist as a %s item! Attempting falling back to vanilla item!", this.item, impl);

            new VanillaItemStack(this.builder, this.item).init();
        });

        return this;
    }

    @Override
    public @NonNull String getImpl() {
        return "CraftEngine";
    }
}