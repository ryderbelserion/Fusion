package com.ryderbelserion.fusion.paper.api.builder.gui;

import com.ryderbelserion.fusion.paper.api.builder.gui.enums.GuiScrollType;
import com.ryderbelserion.fusion.paper.api.builder.gui.types.ScrollingGui;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;

public final class ScrollingBuilder extends BaseGuiBuilder<ScrollingGui, ScrollingBuilder> {

    private GuiScrollType scrollType;
    private int pageSize = 0;

    public ScrollingBuilder(@NotNull final GuiScrollType scrollType) {
        this.scrollType = scrollType;
    }

    @NotNull
    @Contract("_ -> this")
    public ScrollingBuilder scrollType(@NotNull final GuiScrollType scrollType) {
        this.scrollType = scrollType;

        return this;
    }

    @NotNull
    @Contract("_ -> this")
    public ScrollingBuilder pageSize(final int pageSize) {
        this.pageSize = pageSize;

        return this;
    }

    @NotNull
    @Override
    @Contract(" -> new")
    public ScrollingGui create() {
        final ScrollingGui gui = new ScrollingGui(createContainer(), this.pageSize, this.scrollType, getComponents());

        final Consumer<ScrollingGui> consumer = getConsumer();

        if (consumer != null) consumer.accept(gui);

        return gui;
    }
}