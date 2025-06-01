package com.ryderbelserion.fusion.paper.api.builders.items.types.fireworks;

import com.ryderbelserion.fusion.paper.api.builders.items.BaseItemBuilder;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Fireworks;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class FireworkBuilder extends BaseItemBuilder<FireworkBuilder> {

    private final Fireworks.Builder builder;

    public FireworkBuilder(@NotNull ItemStack item) {
        super(item);

        this.builder = Fireworks.fireworks();
    }

    public @NotNull FireworkBuilder addEffect(@NotNull FireworkEffect effect) {
        this.builder.addEffect(effect);

        return this;
    }

    public @NotNull FireworkBuilder addEffect(boolean flicker, boolean trail, @NotNull FireworkEffect.Type type, @Nullable List<Color> colors, @Nullable List<Color> fadeColors) {
        final FireworkStarBuilder builder = new FireworkStarBuilder(getItem());

        builder.flicker(flicker);
        builder.trail(trail);
        builder.with(type);

        if (colors != null) {
            builder.withColor(colors);
        }

        if (fadeColors != null) {
            builder.withFade(fadeColors);
        }

        return addEffect(builder.getBuilder().build());
    }

    public @NotNull FireworkBuilder withDuration(int duration) {
        this.builder.flightDuration(duration);

        return this;
    }

    @Override
    public @NotNull FireworkBuilder build() {
        getItem().setData(DataComponentTypes.FIREWORKS, this.builder.build());

        return this;
    }
}