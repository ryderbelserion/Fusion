package com.ryderbelserion.fusion.paper.builders.items.types;

import com.ryderbelserion.fusion.paper.builders.items.BaseItemBuilder;
import com.ryderbelserion.fusion.paper.utils.ColorUtils;
import com.ryderbelserion.fusion.paper.utils.ItemUtils;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.BannerPatternLayers;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;

public class PatternBuilder extends BaseItemBuilder<PatternBuilder> {

    private final BannerPatternLayers.Builder builder;

    public PatternBuilder(@NonNull final ItemStack itemStack) {
        super(itemStack);

        this.builder = BannerPatternLayers.bannerPatternLayers();
    }

    public @NonNull PatternBuilder addPattern(@NonNull final Pattern pattern) {
        this.builder.add(pattern);

        return this;
    }

    public @NonNull PatternBuilder addPattern(@NonNull final String pattern, @NonNull final String dye) {
        if (pattern.isBlank() || dye.isBlank()) return this;

        final PatternType type = ItemUtils.getPatternType(pattern.toLowerCase());

        if (type == null) return this;

        return addPattern(new Pattern(ColorUtils.getDyeColor(dye), type));
    }

    @Override
    public @NonNull PatternBuilder build() {
        this.itemStack.setData(DataComponentTypes.BANNER_PATTERNS, this.builder.build());

        return this;
    }
}