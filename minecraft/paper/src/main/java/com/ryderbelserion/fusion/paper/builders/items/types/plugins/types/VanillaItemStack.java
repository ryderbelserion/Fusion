package com.ryderbelserion.fusion.paper.builders.items.types.plugins.types;

import com.ryderbelserion.fusion.paper.builders.items.BaseItemBuilder;
import com.ryderbelserion.fusion.paper.builders.items.types.plugins.ICustomItem;
import com.ryderbelserion.fusion.paper.utils.ItemUtils;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import java.util.Optional;

@NullMarked
public final class VanillaItemStack extends ICustomItem {

    public VanillaItemStack(final BaseItemBuilder builder, final String item) {
        super(builder, item, true);
    }

    @Override
    public Optional<ItemStack> getItemStack() {
        return Optional.of(this.builder.getItemStack());
    }

    @Override
    public VanillaItemStack init() {
        ItemUtils.getItemType(this.item).ifPresentOrElse(this.builder::withType, () -> this.builder.withBase64(this.item));
        
        return this;
    }

    @Override
    public String getImpl() {
        return "Vanilla";
    }
}