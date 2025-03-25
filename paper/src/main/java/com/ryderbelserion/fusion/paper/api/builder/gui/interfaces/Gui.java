package com.ryderbelserion.fusion.paper.api.builder.gui.interfaces;

import com.ryderbelserion.fusion.paper.api.builder.gui.PaginatedBuilder;
import com.ryderbelserion.fusion.paper.api.builder.gui.SimpleBuilder;
import com.ryderbelserion.fusion.paper.api.builder.gui.enums.InteractionComponent;
import com.ryderbelserion.fusion.paper.api.builder.gui.types.BaseGui;
import org.jetbrains.annotations.NotNull;
import java.util.Set;

public final class Gui extends BaseGui {

    public Gui(final String title, final int rows, final Set<InteractionComponent> components) {
        super(title, rows, components);
    }

    public Gui(final String title, final GuiType guiType, final Set<InteractionComponent> components) {
        super(title, guiType, components);
    }

    public static SimpleBuilder gui(@NotNull final GuiType type) {
        return new SimpleBuilder(type);
    }

    public static SimpleBuilder gui() {
        return gui(GuiType.CHEST);
    }

    public static PaginatedBuilder paginated() {
        return new PaginatedBuilder();
    }
}