package com.ryderbelserion.fusion.paper.builders.items.types;

import com.ryderbelserion.fusion.paper.builders.items.BaseItemBuilder;
import com.ryderbelserion.fusion.paper.utils.ColorUtils;
import com.ryderbelserion.fusion.paper.utils.ItemUtils;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.BannerPatternLayers;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PatternBuilder extends BaseItemBuilder<PatternBuilder> {

    private final BannerPatternLayers.Builder builder;

    public PatternBuilder(final ItemStack itemStack) {
        super(itemStack);

        this.builder = BannerPatternLayers.bannerPatternLayers();
    }

    public PatternBuilder addPattern(final Pattern pattern) {
        this.builder.add(pattern);

        return this;
    }

    public PatternBuilder addPattern(final String pattern, final String dye) {
        ItemUtils.getPatternType(pattern.toLowerCase()).ifPresent(type -> addPattern(new Pattern(ColorUtils.getDyeColor(dye), type)));

        return this;
    }

    @Override
    public PatternBuilder build() {
        this.itemStack.setData(DataComponentTypes.BANNER_PATTERNS, this.builder.build());

        return this;
    }
}