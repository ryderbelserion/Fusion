package com.ryderbelserion.paper.builder.items.modern.types.fireworks;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FireworkEffectBuilder {

    private FireworkEffect.Builder builder;

    public FireworkEffectBuilder() {
        this.builder = FireworkEffect.builder();
    }

    public FireworkEffectBuilder withType(FireworkEffect.Type type) {
        this.builder.with(type);

        return this;
    }

    public FireworkEffectBuilder withFlicker(final boolean value) {
        this.builder.flicker(value);

        return this;
    }

    public FireworkEffectBuilder withTrail(final boolean value) {
        this.builder.trail(value);

        return this;
    }

    public FireworkEffectBuilder withColor(@NotNull final List<Color> colors) {
        if (colors.isEmpty()) return this;

        this.builder.withColor(colors);

        return this;
    }

    public FireworkEffectBuilder withFade(@NotNull final List<Color> colors) {
        if (colors.isEmpty()) return this;

        this.builder.withFade(colors);

        return this;
    }

    public FireworkEffect build() {
        return this.builder.build();
    }
}