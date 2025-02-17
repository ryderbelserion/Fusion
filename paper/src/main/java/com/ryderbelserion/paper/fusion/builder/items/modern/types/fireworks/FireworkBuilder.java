package com.ryderbelserion.paper.fusion.builder.items.modern.types.fireworks;

import com.ryderbelserion.paper.fusion.builder.items.modern.BaseItemBuilder;
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

    public FireworkBuilder addEffect(@NotNull final FireworkEffect effect) {
        this.builder.addEffect(effect);

        return this;
    }

    public FireworkBuilder addEffect(final boolean flicker, final boolean trail, final FireworkEffect.Type type, @Nullable final List<Color> colors, @Nullable final List<Color> fadeColors) {
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

    public FireworkBuilder withDuration(final int duration) {
        this.builder.flightDuration(duration);

        return this;
    }

    @Override
    public FireworkBuilder build() {
        getItem().setData(DataComponentTypes.FIREWORKS, this.builder.build());

        return this;
    }
}