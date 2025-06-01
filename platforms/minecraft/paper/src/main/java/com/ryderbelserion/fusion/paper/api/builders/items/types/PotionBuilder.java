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

    public PotionBuilder(@NotNull ItemStack item) {
        super(item);

        this.builder = PotionContents.potionContents();
    }

    public @NotNull PotionBuilder withPotionEffect(@NotNull PotionEffectType potionEffectType, int duration, int amplifier, boolean isAmbient, boolean isParticles, boolean hasIcon) {
        this.builder.addCustomEffect(new PotionEffect(potionEffectType, duration, amplifier).withAmbient(isAmbient).withParticles(isParticles).withIcon(hasIcon));

        return this;
    }

    public @NotNull PotionBuilder withPotionEffect(@NotNull PotionEffectType potionEffectType, int duration, int amplifier) {
        return withPotionEffect(potionEffectType, duration, amplifier, true, true, true);
    }

    public @NotNull PotionBuilder withPotionType(@NotNull PotionType potionType) {
        this.builder.potion(potionType);

        return this;
    }

    public @NotNull PotionBuilder withCustomName(@NotNull String customName) {
        this.builder.customName(customName);

        return this;
    }

    @Override
    public @NotNull PotionBuilder setColor(@NotNull String value) {
        this.builder.customColor(ColorUtils.getColor(value));

        return this;
    }

    @Override
    public @NotNull PotionBuilder build() {
        getItem().setData(DataComponentTypes.POTION_CONTENTS, this.builder.build());

        return this;
    }
}