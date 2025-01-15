package com.ryderbelserion.paper.builder.items.modern.types;

import com.ryderbelserion.paper.builder.items.modern.BaseItemBuilder;
import com.ryderbelserion.paper.util.PaperMethods;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.BannerPatternLayers;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PatternBuilder extends BaseItemBuilder<PatternBuilder> {

    private final BannerPatternLayers.Builder builder;

    public PatternBuilder(@NotNull final ItemStack itemStack) {
        super(itemStack);

        this.builder = BannerPatternLayers.bannerPatternLayers();
    }

    public PatternBuilder addPattern(@NotNull final Pattern pattern) {
        this.builder.add(pattern);

        return this;
    }

    public PatternBuilder addPattern(@NotNull final String pattern, @NotNull final String dye) {
        final PatternType type = PaperMethods.getPatternType(pattern.toLowerCase());

        if (type == null) return this;

        final DyeColor color = PaperMethods.getDyeColor(dye);

        return addPattern(new Pattern(color, type));
    }

    @Override
    public PatternBuilder build() {
        getItem().setData(DataComponentTypes.BANNER_PATTERNS, this.builder.build());

        return this;
    }
}