package com.ryderbelserion.fusion.paper.builders.types.fireworks;

import com.ryderbelserion.fusion.paper.builders.BaseItemBuilder;
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

    public FireworkBuilder(@NotNull final ItemStack itemStack) {
        super(itemStack);

        this.builder = Fireworks.fireworks();
    }

    public @NotNull FireworkBuilder addEffect(@NotNull final FireworkEffect effect) {
        this.builder.addEffect(effect);

        return this;
    }

    public @NotNull FireworkBuilder addEffect(final boolean flicker, final boolean trail, @NotNull final FireworkEffect.Type type, @Nullable final List<Color> colors, @Nullable final List<Color> fadeColors) {
        final FireworkStarBuilder builder = new FireworkStarBuilder(this.itemStack);

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

    public @NotNull FireworkBuilder withDuration(final int duration) {
        this.builder.flightDuration(duration);

        return this;
    }

    @Override
    public @NotNull FireworkBuilder build() {
        this.itemStack.setData(DataComponentTypes.FIREWORKS, this.builder.build());

        return this;
    }
}