package com.ryderbelserion.fusion;

import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.builders.ItemBuilder;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Fusion extends JavaPlugin {

    private final FusionPaper fusion;

    public Fusion(@NotNull final FusionPaper fusion) {
        this.fusion = fusion;
    }

    @Override
    public void onEnable() {
        this.fusion.setJavaPlugin(this).init();

        @NotNull final ItemStack itemBuilder = new ItemBuilder(ItemType.STONE).withConsumer(consumer -> {
            consumer.withDisplayName("This is a display name");
        }).asItemStack();

        final ComponentLogger logger = getComponentLogger();

        if (itemBuilder.isEmpty()) {
            logger.warn("This item is item.");
        } else {
            logger.warn("This item {}, {} is not empty.", itemBuilder.getType(), PlainTextComponentSerializer.plainText().serialize(itemBuilder.effectiveName()));
        }
    }

    public @NotNull final FusionPaper getFusion() {
        return this.fusion;
    }
}