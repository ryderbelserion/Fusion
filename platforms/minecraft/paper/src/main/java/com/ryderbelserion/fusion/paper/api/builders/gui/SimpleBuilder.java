package com.ryderbelserion.fusion.paper.api.builders.gui;

import com.ryderbelserion.fusion.paper.api.builders.gui.interfaces.Gui;
import com.ryderbelserion.fusion.paper.api.builders.gui.interfaces.GuiType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;

public class SimpleBuilder extends BaseGuiBuilder<Gui, SimpleBuilder> {

    private final Plugin plugin;
    private GuiType guiType;

    public SimpleBuilder(@NotNull Plugin plugin, @NotNull GuiType guiType) {
        this.guiType = guiType;
        this.plugin = plugin;
    }

    @Override
    public @NotNull Gui create() {
        Gui gui;

        gui = this.guiType == null || this.guiType == GuiType.CHEST ? new Gui(this.plugin, getTitle(), getRows(), getInteractionComponents()) : new Gui(this.plugin, getTitle(), this.guiType, getInteractionComponents());

        Consumer<Gui> consumer = getConsumer();
        if (consumer != null) consumer.accept(gui);

        return gui;
    }

    public @NotNull SimpleBuilder setType(@NotNull GuiType guiType) {
        this.guiType = guiType;

        return this;
    }
}