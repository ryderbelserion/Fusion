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
import java.util.List;

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

    public PatternBuilder addPattern(@NotNull final String pattern) {
        if (!pattern.contains(":")) return this;

        final String[] sections = pattern.split(":");

        final PatternType type = PaperMethods.getPatternType(sections[0].toLowerCase());

        if (type == null) return this;

        final DyeColor color = PaperMethods.getDyeColor(sections[1]);

        return addPattern(new Pattern(color, type));
    }

    public PatternBuilder addPatterns(@NotNull final List<String> patterns) {
        patterns.forEach(this::addPattern);

        return this;
    }

    @Override
    public PatternBuilder build() {
        getItem().setData(DataComponentTypes.BANNER_PATTERNS, this.builder.build());

        return this;
    }
}