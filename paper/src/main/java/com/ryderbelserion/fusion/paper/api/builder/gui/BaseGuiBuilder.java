package com.ryderbelserion.fusion.paper.api.builder.gui;

import com.ryderbelserion.fusion.paper.FusionPlugin;
import com.ryderbelserion.fusion.paper.api.builder.gui.interfaces.GuiContainer;
import com.ryderbelserion.fusion.paper.api.builder.gui.interfaces.GuiProvider;
import com.ryderbelserion.fusion.paper.api.builder.gui.types.BaseGui;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public abstract class BaseGuiBuilder<G extends BaseGui, B extends BaseGuiBuilder<G, B>> extends GuiBuilder<G, B> {

    private final Plugin plugin = FusionPlugin.getPlugin();

    private GuiProvider.Chest guiProvider = (title, owner, rows) -> this.plugin.getServer().createInventory(owner, rows, title);
    private int rows = 1;

    public final B inventory(@NotNull final GuiProvider.Chest guiProvider) {
        this.guiProvider = guiProvider;

        return (B) this;
    }

    public final int getRows() {
        return this.rows;
    }

    public @NotNull final B setRows(final int rows) {
        this.rows = rows;

        return (B) this;
    }

    protected @NotNull GuiProvider.Chest getGuiProvider() {
        return this.guiProvider;
    }

    protected @NotNull GuiContainer.Chest createContainer() {
        return new GuiContainer.Chest(getTitle(), this.guiProvider, getRows());
    }
}