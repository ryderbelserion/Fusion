package com.ryderbelserion.fusion.paper.api.builder.gui.types;

import com.ryderbelserion.fusion.paper.api.builder.gui.BaseGuiBuilder;
import com.ryderbelserion.fusion.paper.api.builder.gui.objects.Gui;
import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;

public final class ChestGui extends BaseGuiBuilder<Gui, ChestGui> {

    @Override
    public @NotNull Gui create() {
        final Gui gui = new Gui(createContainer(), getInteractionComponents());

        final Consumer<Gui> consumer = getConsumer();

        if (consumer != null) consumer.accept(gui);

        return gui;
    }
}