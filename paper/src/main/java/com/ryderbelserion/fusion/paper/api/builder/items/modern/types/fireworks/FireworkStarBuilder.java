package com.ryderbelserion.fusion.paper.api.builder.items.modern.types.fireworks;

import com.ryderbelserion.fusion.paper.api.builder.items.modern.BaseItemBuilder;
import io.papermc.paper.datacomponent.DataComponentTypes;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class FireworkStarBuilder extends BaseItemBuilder<FireworkStarBuilder> {

    private final FireworkEffect.Builder builder;

    public FireworkStarBuilder(@NotNull final ItemStack itemStack) {
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

    public FireworkStarBuilder withColor(@NotNull final Color... colors) {
        this.builder.withColor(colors);

        return this;
    }

    public FireworkStarBuilder withColor(@NotNull final List<Color> colors) {
        this.builder.withColor(colors);

        return this;
    }

    public FireworkStarBuilder withFade(final Color color) {
        this.builder.withFade(color);

        return this;
    }

    public FireworkStarBuilder withFade(@NotNull final Color... colors) {
        this.builder.withFade(colors);

        return this;
    }

    public FireworkStarBuilder withFade(@NotNull final List<Color> colors) {
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
        getItem().setData(DataComponentTypes.FIREWORK_EXPLOSION, this.builder.build());

        return this;
    }
}