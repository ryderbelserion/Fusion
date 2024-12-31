package com.ryderbelserion.paper.builder.items.modern.types;

import com.ryderbelserion.paper.builder.items.modern.BaseItemBuilder;
import com.ryderbelserion.paper.util.PaperMethods;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.PotionContents;
import org.bukkit.Color;
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

    public PotionBuilder withPotionEffect(final PotionEffectType potionEffectType, final int duration, final int amplifier, final boolean isAmbient, final boolean isParticles, final boolean hasIcon) {
        this.builder.addCustomEffect(new PotionEffect(potionEffectType, duration, amplifier).withAmbient(isAmbient).withParticles(isParticles).withIcon(hasIcon));

        return this;
    }

    public PotionBuilder withPotionEffect(final PotionEffectType potionEffectType, final int duration, final int amplifier) {
        return withPotionEffect(potionEffectType, duration, amplifier, true, true, true);
    }

    public PotionBuilder withPotionType(final PotionType potionType) {
        this.builder.potion(potionType);

        return this;
    }

    public PotionBuilder withCustomName(final String customName) {
        this.builder.customName(customName);

        return this;
    }

    @Override
    public PotionBuilder setColor(final @NotNull String value) {
        this.builder.customColor(PaperMethods.getColor(value));

        return this;
    }

    @Override
    public PotionBuilder build() {
        getItem().setData(DataComponentTypes.POTION_CONTENTS, this.builder.build());

        return this;
    }
}