package com.ryderbelserion.paper.builder.gui;

import com.ryderbelserion.paper.builder.gui.types.PaginatedGui;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;

public final class PaginatedBuilder extends BaseGuiBuilder<PaginatedGui, PaginatedBuilder> {

    private int pageSize = 0;

    public PaginatedBuilder() {}

    @NotNull
    @Contract("_ -> this")
    public PaginatedBuilder pageSize(final int pageSize) {
        this.pageSize = pageSize;

        return this;
    }

    @Override
    public @NotNull PaginatedGui create() {
        final PaginatedGui gui = new PaginatedGui(getTitle(), this.pageSize, getRows(), getInteractionComponents());

        final Consumer<PaginatedGui> consumer = getConsumer();

        if (consumer != null) consumer.accept(gui);

        return gui;
    }
}