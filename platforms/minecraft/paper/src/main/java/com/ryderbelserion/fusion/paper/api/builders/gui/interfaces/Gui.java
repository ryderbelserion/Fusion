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

    public Gui(@NotNull Plugin plugin, @NotNull Audience audience, @NotNull String title, @NotNull GuiType guiType, @NotNull Set<InteractionComponent> components) {
        super(plugin, audience, title, guiType, components);
    }

    public Gui(@NotNull Plugin plugin, @NotNull String title, @NotNull GuiType guiType, @NotNull Set<InteractionComponent> components) {
        this(plugin, Audience.empty(), title, guiType, components);
    }

    public Gui(@NotNull Plugin plugin, @NotNull Audience audience, @NotNull String title, int rows, @NotNull Set<InteractionComponent> components) {
        super(plugin, audience, title, rows, components);
    }

    public Gui(@NotNull Plugin plugin, @NotNull String title, int rows, @NotNull Set<InteractionComponent> components) {
        this(plugin, Audience.empty(), title, rows, components);
    }

    public static @NotNull SimpleBuilder gui(@NotNull Plugin plugin, @NotNull GuiType type) {
        return new SimpleBuilder(plugin, type);
    }

    public static @NotNull SimpleBuilder gui(@NotNull Plugin plugin) {
        return gui(plugin, GuiType.CHEST);
    }

    public static @NotNull PaginatedBuilder paginated(@NotNull Plugin plugin) {
        return new PaginatedBuilder(plugin);
    }
}