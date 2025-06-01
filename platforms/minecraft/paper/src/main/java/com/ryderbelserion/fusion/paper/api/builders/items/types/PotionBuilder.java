package com.ryderbelserion.fusion.paper.api.builders.items.types;

import com.ryderbelserion.fusion.paper.api.builders.items.BaseItemBuilder;
import com.ryderbelserion.fusion.paper.utils.ColorUtils;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.PotionContents;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

public class PotionBuilder extends BaseItemBuilder<PotionBuilder> {

    private final PotionContents.Builder builder;

    public PotionBuilder(@NotNull final ItemStack itemStack) {
        super(itemStack);

        this.builder = PotionContents.potionContents();
    }

    public @NotNull PotionBuilder withPotionEffect(@NotNull final PotionEffectType potionEffectType, final int duration, final int amplifier, final boolean isAmbient, final boolean isParticles, final boolean hasIcon) {
        this.builder.addCustomEffect(new PotionEffect(potionEffectType, duration, amplifier).withAmbient(isAmbient).withParticles(isParticles).withIcon(hasIcon));

        return this;
    }

    public @NotNull PotionBuilder withPotionEffect(@NotNull final PotionEffectType potionEffectType, final int duration, final int amplifier) {
        return withPotionEffect(potionEffectType, duration, amplifier, true, true, true);
    }

    public @NotNull PotionBuilder withPotionType(@NotNull final PotionType potionType) {
        this.builder.potion(potionType);

        return this;
    }

    public @NotNull PotionBuilder withCustomName(@NotNull final String customName) {
        this.builder.customName(customName);

        return this;
    }

    @Override
    public @NotNull PotionBuilder setColor(@NotNull final String value) {
        this.builder.customColor(ColorUtils.getColor(value));

        return this;
    }

    @Override
    public @NotNull PotionBuilder build() {
        getItem().setData(DataComponentTypes.POTION_CONTENTS, this.builder.build());

        return this;
    }
}