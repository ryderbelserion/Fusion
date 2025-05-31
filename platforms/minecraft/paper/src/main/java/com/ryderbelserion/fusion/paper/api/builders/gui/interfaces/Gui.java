package com.ryderbelserion.fusion.paper.api.builders.gui.interfaces;

import com.ryderbelserion.fusion.paper.api.builders.gui.PaginatedBuilder;
import com.ryderbelserion.fusion.paper.api.builders.gui.SimpleBuilder;
import com.ryderbelserion.fusion.paper.api.builders.gui.enums.InteractionComponent;
import com.ryderbelserion.fusion.paper.api.builders.gui.types.BaseGui;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;
import java.util.Set;

public final class Gui extends BaseGui {

    public Gui(@NotNull final Audience audience, @NotNull final String title, @NotNull final GuiType guiType, @NotNull final Set<InteractionComponent> components) {
        super(audience, title, guiType, components);
    }

    public Gui(@NotNull final String title, @NotNull final GuiType guiType, @NotNull final Set<InteractionComponent> components) {
        this(Audience.empty(), title, guiType, components);
    }

    public Gui(@NotNull final Audience audience, @NotNull final String title, final int rows, @NotNull final Set<InteractionComponent> components) {
        super(audience, title, rows, components);
    }

    public Gui(@NotNull final String title, final int rows, @NotNull final Set<InteractionComponent> components) {
        this(Audience.empty(), title, rows, components);
    }

    public static @NotNull SimpleBuilder gui(@NotNull final GuiType type) {
        return new SimpleBuilder(type);
    }

    public static @NotNull SimpleBuilder gui() {
        return gui(GuiType.CHEST);
    }

    public static @NotNull PaginatedBuilder paginated() {
        return new PaginatedBuilder();
    }
}