package com.ryderbelserion.fusion.paper.api.builders.items.types.fireworks;

import com.ryderbelserion.fusion.paper.api.builders.items.BaseItemBuilder;
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

    public @NotNull FireworkStarBuilder flicker(final boolean flicker) {
        this.builder.flicker(flicker);

        return this;
    }

    public @NotNull FireworkStarBuilder trail(final boolean trail) {
        this.builder.trail(trail);

        return this;
    }

    public @NotNull FireworkStarBuilder withColor(@NotNull final Color color) {
        this.builder.withColor(color);

        return this;
    }

    public @NotNull FireworkStarBuilder withColor(@NotNull final Color... colors) {
        this.builder.withColor(colors);

        return this;
    }

    public @NotNull FireworkStarBuilder withColor(@NotNull final List<Color> colors) {
        this.builder.withColor(colors);

        return this;
    }

    public @NotNull FireworkStarBuilder withFade(@NotNull final Color color) {
        this.builder.withFade(color);

        return this;
    }

    public @NotNull FireworkStarBuilder withFade(@NotNull final Color... colors) {
        this.builder.withFade(colors);

        return this;
    }

    public @NotNull FireworkStarBuilder withFade(@NotNull final List<Color> colors) {
        this.builder.withFade(colors);

        return this;
    }

    public @NotNull FireworkStarBuilder with(@NotNull final FireworkEffect.Type type) {
        this.builder.with(type);

        return this;
    }

    public @NotNull FireworkEffect.Builder getBuilder() {
        return this.builder;
    }

    @Override
    public @NotNull FireworkStarBuilder build() {
        getItem().setData(DataComponentTypes.FIREWORK_EXPLOSION, this.builder.build());

        return this;
    }
}