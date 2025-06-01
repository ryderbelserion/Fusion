package com.ryderbelserion.fusion.paper.api.builders.gui.interfaces;

import com.ryderbelserion.fusion.paper.api.builders.gui.PaginatedBuilder;
import com.ryderbelserion.fusion.paper.api.builders.gui.SimpleBuilder;
import com.ryderbelserion.fusion.paper.api.builders.gui.enums.InteractionComponent;
import com.ryderbelserion.fusion.paper.api.builders.gui.types.BaseGui;
import net.kyori.adventure.audience.Audience;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import java.util.Set;

public class Gui extends BaseGui {

    public Gui(@NotNull final Plugin plugin, @NotNull final Audience audience, @NotNull final String title, @NotNull final GuiType guiType, @NotNull final Set<InteractionComponent> components) {
        super(plugin, audience, title, guiType, components);
    }

    public Gui(@NotNull final Plugin plugin, @NotNull final String title, @NotNull final GuiType guiType, @NotNull final Set<InteractionComponent> components) {
        this(plugin, Audience.empty(), title, guiType, components);
    }

    public Gui(@NotNull final Plugin plugin, @NotNull final Audience audience, @NotNull final String title, final int rows, @NotNull final Set<InteractionComponent> components) {
        super(plugin, audience, title, rows, components);
    }

    public Gui(@NotNull final Plugin plugin, @NotNull final String title, final int rows, @NotNull final Set<InteractionComponent> components) {
        this(plugin, Audience.empty(), title, rows, components);
    }

    public static @NotNull SimpleBuilder gui(@NotNull final Plugin plugin, @NotNull final GuiType type) {
        return new SimpleBuilder(plugin, type);
    }

    public static @NotNull SimpleBuilder gui(@NotNull final Plugin plugin) {
        return gui(plugin, GuiType.CHEST);
    }

    public static @NotNull PaginatedBuilder paginated(@NotNull final Plugin plugin) {
        return new PaginatedBuilder(plugin);
    }
}