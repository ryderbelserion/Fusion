package com.ryderbelserion.fusion.paper.api.builders.gui;

import com.ryderbelserion.fusion.paper.api.builders.gui.interfaces.Gui;
import com.ryderbelserion.fusion.paper.api.builders.gui.interfaces.GuiType;
import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;

public final class SimpleBuilder extends BaseGuiBuilder<Gui, SimpleBuilder> {

    private GuiType guiType;

    public SimpleBuilder(@NotNull final GuiType guiType) {
        this.guiType = guiType;
    }

    @Override
    public @NotNull Gui create() {
        final Gui gui;

        gui = this.guiType == null || this.guiType == GuiType.CHEST ? new Gui(getTitle(), getRows(), getInteractionComponents()) : new Gui(getTitle(), this.guiType, getInteractionComponents());

        final Consumer<Gui> consumer = getConsumer();
        if (consumer != null) consumer.accept(gui);

        return gui;
    }

    public @NotNull SimpleBuilder setType(final GuiType guiType) {
        this.guiType = guiType;

        return this;
    }
}