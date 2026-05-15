package com.ryderbelserion.fusion.paper.builders.items.types;

import com.ryderbelserion.fusion.paper.builders.items.BaseItemBuilder;
import com.ryderbelserion.fusion.paper.utils.ColorUtils;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.PotionContents;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jspecify.annotations.NonNull;

public class PotionBuilder extends BaseItemBuilder<PotionBuilder> {

    private final PotionContents.Builder builder;

    public PotionBuilder(@NonNull final ItemStack itemStack) {
        super(itemStack);

        this.builder = PotionContents.potionContents();
    }

    public @NonNull PotionBuilder withPotionEffect(@NonNull final PotionEffectType potionEffectType, final int duration, final int amplifier, final boolean isAmbient, final boolean isParticles, final boolean hasIcon) {
        this.builder.addCustomEffect(new PotionEffect(potionEffectType, duration, amplifier).withAmbient(isAmbient).withParticles(isParticles).withIcon(hasIcon));

        return this;
    }

    public @NonNull PotionBuilder withPotionEffect(@NonNull final PotionEffectType potionEffectType, final int duration, final int amplifier) {
        return withPotionEffect(potionEffectType, duration, amplifier, true, true, true);
    }

    public @NonNull PotionBuilder withPotionType(@NonNull final PotionType potionType) {
        this.builder.potion(potionType);

        return this;
    }

    public @NonNull PotionBuilder withCustomName(@NonNull final String customName) {
        if (customName.isBlank()) return this;

        this.builder.customName(customName);

        return this;
    }

    public @NonNull PotionBuilder setDuration(final float duration) {
        if (duration == -1F) return this;

        this.itemStack.setData(DataComponentTypes.POTION_DURATION_SCALE, duration);

        return this;
    }

    @Override
    public @NonNull PotionBuilder setColor(@NonNull final String value) {
        if (value.isBlank()) return this;

        this.builder.customColor(value.contains(",") ? ColorUtils.getRGB(value) : ColorUtils.getColor(value));

        return this;
    }

    @Override
    public @NonNull PotionBuilder build() {
        this.itemStack.setData(DataComponentTypes.POTION_CONTENTS, this.builder.build());

        return this;
    }
}