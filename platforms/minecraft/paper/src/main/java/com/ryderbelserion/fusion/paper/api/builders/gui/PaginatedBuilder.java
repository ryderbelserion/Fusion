package com.ryderbelserion.fusion.paper.api.builders.gui;

import com.ryderbelserion.fusion.paper.api.builders.gui.types.PaginatedGui;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;

public final class PaginatedBuilder extends BaseGuiBuilder<PaginatedGui, PaginatedBuilder> {

    private final Plugin plugin;
    private int pageSize = 0;

    public PaginatedBuilder(@NotNull final Plugin plugin) {
        this.plugin = plugin;
    }

    @NotNull
    @Contract("_ -> this")
    public PaginatedBuilder pageSize(final int pageSize) {
        this.pageSize = pageSize;

        return this;
    }

    @Override
    public @NotNull PaginatedGui create() {
        final PaginatedGui gui = new PaginatedGui(this.plugin, getTitle(), this.pageSize, getRows(), getInteractionComponents());

        final Consumer<PaginatedGui> consumer = getConsumer();

        if (consumer != null) consumer.accept(gui);

        return gui;
    }
}