package com.ryderbelserion.fusion.paper.builders.items.types.fireworks;

import com.ryderbelserion.fusion.paper.builders.items.BaseItemBuilder;
import io.papermc.paper.datacomponent.DataComponentTypes;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import java.util.List;

@NullMarked
public final class FireworkStarBuilder extends BaseItemBuilder<FireworkStarBuilder> {

    private final FireworkEffect.Builder builder;

    public FireworkStarBuilder(final ItemStack itemStack) {
        super(itemStack);

        this.builder = FireworkEffect.builder();
    }

    public FireworkStarBuilder flicker(final boolean flicker) {
        this.builder.flicker(flicker);

        return this;
    }

    public FireworkStarBuilder trail(final boolean trail) {
        this.builder.trail(trail);

        return this;
    }

    public FireworkStarBuilder withColor(final Color color) {
        this.builder.withColor(color);

        return this;
    }

    public FireworkStarBuilder withColor(final Color... colors) {
        this.builder.withColor(colors);

        return this;
    }

    public FireworkStarBuilder withColor(final List<Color> colors) {
        this.builder.withColor(colors);

        return this;
    }

    public FireworkStarBuilder withFade(final Color color) {
        this.builder.withFade(color);

        return this;
    }

    public FireworkStarBuilder withFade(final Color... colors) {
        this.builder.withFade(colors);

        return this;
    }

    public FireworkStarBuilder withFade(final List<Color> colors) {
        this.builder.withFade(colors);

        return this;
    }

    public FireworkStarBuilder with(final FireworkEffect.Type type) {
        this.builder.with(type);

        return this;
    }

    public FireworkEffect.Builder getBuilder() {
        return this.builder;
    }

    @Override
    public FireworkStarBuilder build() {
        this.itemStack.setData(DataComponentTypes.FIREWORK_EXPLOSION, this.builder.build());

        return this;
    }
}