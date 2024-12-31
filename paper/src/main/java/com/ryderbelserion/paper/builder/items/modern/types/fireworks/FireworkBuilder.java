package com.ryderbelserion.paper.builder.items.modern.types.fireworks;

import com.ryderbelserion.paper.builder.items.modern.BaseItemBuilder;
import com.ryderbelserion.util.Methods;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Fireworks;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class FireworkBuilder extends BaseItemBuilder<FireworkBuilder> {

    private final Fireworks.Builder builder;

    public FireworkBuilder(@NotNull final ItemStack itemStack) {
        super(itemStack);

        this.builder = Fireworks.fireworks();
    }

    public void addEffect(@NotNull final FireworkEffect effect) {
        this.builder.addEffect(effect);
    }

    public void addEffect(final String effect) {
        final String[] split = effect.split(":");

        this.logger.warn("{}", split[0]);

        switch (split[0]) {
            case "flicker" -> {

            }

            case "trail" -> {

            }

            case "fade" -> {

            }

            case "color" -> {

            }
        }

        //final boolean withFlicker, final boolean withTrail, final List<Color> colors, final List<Color> fadeColors

        //final FireworkEffectBuilder builder = new FireworkEffectBuilder();

        //addEffect(builder.withFlicker(withFlicker).withTrail(withTrail).withColor(colors).withFade(fadeColors).build());
    }

    public void addEffects(@NotNull final List<String> effects) {
        effects.forEach(this::addEffect);
    }

    @Override
    public FireworkBuilder build() {
        getItem().setData(DataComponentTypes.FIREWORKS, this.builder.build());

        return this;
    }
}