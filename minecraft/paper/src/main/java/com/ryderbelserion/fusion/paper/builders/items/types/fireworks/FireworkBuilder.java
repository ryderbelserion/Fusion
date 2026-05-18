package com.ryderbelserion.fusion.paper.builders.items.types.fireworks;

import com.ryderbelserion.fusion.paper.builders.items.BaseItemBuilder;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Fireworks;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class FireworkBuilder extends BaseItemBuilder<FireworkBuilder> {

    private final Fireworks.Builder builder;

    public FireworkBuilder(@NonNull final ItemStack itemStack) {
        super(itemStack);

        this.builder = Fireworks.fireworks();
    }

    public @NonNull FireworkBuilder addEffect(@NonNull final FireworkEffect effect) {
        this.builder.addEffect(effect);

        return this;
    }

    public @NonNull FireworkBuilder addEffect(final boolean flicker, final boolean trail, final FireworkEffect.@NonNull Type type, @Nullable final List<Color> colors, @Nullable final List<Color> fadeColors) {
        final FireworkStarBuilder builder = new FireworkStarBuilder(this.itemStack);

        builder.flicker(flicker);
        builder.trail(trail);
        builder.with(type);

        if (colors != null) builder.withColor(colors);

        if (fadeColors != null) builder.withFade(fadeColors);

        return addEffect(builder.getBuilder().build());
    }

    public @NonNull FireworkBuilder withDuration(final int duration) {
        if (duration == -1) return this;

        this.builder.flightDuration(duration);

        return this;
    }

    @Override
    public @NonNull FireworkBuilder build() {
        this.itemStack.setData(DataComponentTypes.FIREWORKS, this.builder.build());

        return this;
    }
}