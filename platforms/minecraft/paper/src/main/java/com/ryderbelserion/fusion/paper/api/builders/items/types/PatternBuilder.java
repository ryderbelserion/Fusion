package com.ryderbelserion.fusion.paper.api.builders.items.types;

import com.ryderbelserion.fusion.paper.api.builders.items.BaseItemBuilder;
import com.ryderbelserion.fusion.paper.utils.ColorUtils;
import com.ryderbelserion.fusion.paper.utils.ItemUtils;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.BannerPatternLayers;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PatternBuilder extends BaseItemBuilder<PatternBuilder> {

    private final BannerPatternLayers.Builder builder;

    public PatternBuilder(@NotNull ItemStack item) {
        super(item);

        this.builder = BannerPatternLayers.bannerPatternLayers();
    }

    public @NotNull PatternBuilder addPattern(@NotNull Pattern pattern) {
        this.builder.add(pattern);

        return this;
    }

    public @NotNull PatternBuilder addPattern(@NotNull String pattern, @NotNull String dye) {
        final PatternType type = ItemUtils.getPatternType(pattern.toLowerCase());

        if (type == null) return this;

        final DyeColor color = ColorUtils.getDyeColor(dye);

        return addPattern(new Pattern(color, type));
    }

    @Override
    public @NotNull PatternBuilder build() {
        getItem().setData(DataComponentTypes.BANNER_PATTERNS, this.builder.build());

        return this;
    }
}