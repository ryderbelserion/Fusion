package com.ryderbelserion.fusion.paper.builders.items.types.fireworks;

import com.ryderbelserion.fusion.paper.builders.items.BaseItemBuilder;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Fireworks;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import java.util.List;

@NullMarked
public final class FireworkBuilder extends BaseItemBuilder<FireworkBuilder> {

    private final Fireworks.Builder builder;

    public FireworkBuilder(final ItemStack itemStack) {
        super(itemStack);

        this.builder = Fireworks.fireworks();
    }

    public FireworkBuilder addEffect(final FireworkEffect effect) {
        this.builder.addEffect(effect);

        return this;
    }

    public FireworkBuilder addEffect(final boolean flicker, final boolean trail, final FireworkEffect.Type type, final List<Color> colors, final List<Color> fadeColors) {
        final FireworkStarBuilder builder = new FireworkStarBuilder(this.itemStack);

        builder.flicker(flicker);
        builder.trail(trail);
        builder.with(type);

        if (!colors.isEmpty()) builder.withColor(colors);

        if (!fadeColors.isEmpty()) builder.withFade(fadeColors);

        return addEffect(builder.getBuilder().build());
    }

    public FireworkBuilder withDuration(final int duration) {
        if (duration == -1) return this;

        this.builder.flightDuration(duration);

        return this;
    }

    @Override
    public FireworkBuilder build() {
        this.itemStack.setData(DataComponentTypes.FIREWORKS, this.builder.build());

        return this;
    }
}