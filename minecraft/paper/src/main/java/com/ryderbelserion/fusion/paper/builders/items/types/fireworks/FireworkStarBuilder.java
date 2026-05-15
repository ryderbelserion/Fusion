package com.ryderbelserion.fusion.paper.builders.items.types.fireworks;

import com.ryderbelserion.fusion.paper.builders.items.BaseItemBuilder;
import io.papermc.paper.datacomponent.DataComponentTypes;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import java.util.List;

public class FireworkStarBuilder extends BaseItemBuilder<FireworkStarBuilder> {

    private final FireworkEffect.Builder builder;

    public FireworkStarBuilder(@NonNull final ItemStack itemStack) {
        super(itemStack);

        this.builder = FireworkEffect.builder();
    }

    public @NonNull FireworkStarBuilder flicker(final boolean flicker) {
        this.builder.flicker(flicker);

        return this;
    }

    public @NonNull FireworkStarBuilder trail(final boolean trail) {
        this.builder.trail(trail);

        return this;
    }

    public @NonNull FireworkStarBuilder withColor(@NonNull final Color color) {
        this.builder.withColor(color);

        return this;
    }

    public @NonNull FireworkStarBuilder withColor(@NonNull final Color... colors) {
        this.builder.withColor(colors);

        return this;
    }

    public @NonNull FireworkStarBuilder withColor(@NonNull final List<Color> colors) {
        this.builder.withColor(colors);

        return this;
    }

    public @NonNull FireworkStarBuilder withFade(@NonNull final Color color) {
        this.builder.withFade(color);

        return this;
    }

    public @NonNull FireworkStarBuilder withFade(@NonNull final Color... colors) {
        this.builder.withFade(colors);

        return this;
    }

    public @NonNull FireworkStarBuilder withFade(@NonNull final List<Color> colors) {
        this.builder.withFade(colors);

        return this;
    }

    public @NonNull FireworkStarBuilder with(@NonNull final FireworkEffect.Type type) {
        this.builder.with(type);

        return this;
    }

    public @NonNull FireworkEffect.Builder getBuilder() {
        return this.builder;
    }

    @Override
    public @NonNull FireworkStarBuilder build() {
        this.itemStack.setData(DataComponentTypes.FIREWORK_EXPLOSION, this.builder.build());

        return this;
    }
}