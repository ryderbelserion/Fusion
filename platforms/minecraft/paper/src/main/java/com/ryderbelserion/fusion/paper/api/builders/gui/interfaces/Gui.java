package com.ryderbelserion.fusion.paper.api.builders.gui.interfaces;

import com.ryderbelserion.fusion.paper.api.builders.gui.PaginatedBuilder;
import com.ryderbelserion.fusion.paper.api.builders.gui.SimpleBuilder;
import com.ryderbelserion.fusion.paper.api.builders.gui.enums.InteractionComponent;
import com.ryderbelserion.fusion.paper.api.builders.gui.types.BaseGui;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;
import java.util.Set;

public class Gui extends BaseGui {

    public Gui(@NotNull Audience audience, @NotNull String title, @NotNull GuiType guiType, @NotNull Set<InteractionComponent> components) {
        super(audience, title, guiType, components);
    }

    public Gui(@NotNull String title, @NotNull GuiType guiType, @NotNull Set<InteractionComponent> components) {
        this(Audience.empty(), title, guiType, components);
    }

    public Gui(@NotNull Audience audience, @NotNull String title, int rows, @NotNull Set<InteractionComponent> components) {
        super(audience, title, rows, components);
    }

    public Gui(@NotNull String title, int rows, @NotNull Set<InteractionComponent> components) {
        this(Audience.empty(), title, rows, components);
    }

    public static @NotNull SimpleBuilder gui(@NotNull GuiType type) {
        return new SimpleBuilder(type);
    }

    public static @NotNull SimpleBuilder gui() {
        return gui(GuiType.CHEST);
    }

    public static @NotNull PaginatedBuilder paginated() {
        return new PaginatedBuilder();
    }
}