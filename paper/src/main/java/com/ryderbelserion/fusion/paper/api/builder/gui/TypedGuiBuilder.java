package com.ryderbelserion.fusion.paper.api.builder.gui;

import com.ryderbelserion.fusion.paper.FusionPlugin;
import com.ryderbelserion.fusion.paper.api.builder.gui.objects.Gui;
import com.ryderbelserion.fusion.paper.api.builder.gui.enums.GuiType;
import com.ryderbelserion.fusion.paper.api.builder.gui.interfaces.GuiContainer;
import com.ryderbelserion.fusion.paper.api.builder.gui.objects.GuiProvider;
import com.ryderbelserion.fusion.paper.api.builder.gui.types.ChestGui;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;

public final class TypedGuiBuilder extends BaseGuiBuilder<Gui, TypedGuiBuilder> {

    private final Plugin plugin = FusionPlugin.getPlugin();

    private GuiProvider.Typed guiProvider = (title, owner, type) -> this.plugin.getServer().createInventory(owner, type, title);
    private GuiType guiType;

    public TypedGuiBuilder(@NotNull final GuiType guiType, @NotNull final ChestGui builder) {
        this.guiType = guiType;

        consumeBuilder(builder);
    }
    
    public TypedGuiBuilder(@NotNull final GuiType guiType) {
        this(guiType, new ChestGui());
    }

    @NotNull
    @Contract("_ -> this")
    public TypedGuiBuilder type(@NotNull final GuiType guiType) {
        this.guiType = guiType;

        return this;
    }

    @NotNull
    @Contract("_ -> this")
    public TypedGuiBuilder inventory(@NotNull final GuiProvider.Typed guiProvider) {
        this.guiProvider = guiProvider;

        return this;
    }

    @NotNull
    @Override
    @Contract(" -> new")
    public Gui create() {
        final Gui gui = new Gui(new GuiContainer.Typed(getTitle(), this.guiProvider, this.guiType), getComponents());

        final Consumer<Gui> consumer = getConsumer();

        if (consumer != null) consumer.accept(gui);

        return gui;
    }
}