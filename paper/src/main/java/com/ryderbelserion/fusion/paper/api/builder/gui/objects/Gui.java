package com.ryderbelserion.fusion.paper.api.builder.gui.objects;

import com.ryderbelserion.fusion.paper.api.builder.gui.PaginatedBuilder;
import com.ryderbelserion.fusion.paper.api.builder.gui.interfaces.GuiContainer;
import com.ryderbelserion.fusion.paper.api.builder.gui.types.ChestGui;
import com.ryderbelserion.fusion.paper.api.builder.gui.enums.GuiComponent;
import com.ryderbelserion.fusion.paper.api.builder.gui.types.BaseGui;
import org.jetbrains.annotations.NotNull;
import java.util.Set;

public final class Gui extends BaseGui {

    public Gui(@NotNull final GuiContainer guiContainer, @NotNull final Set<GuiComponent> components) {
        super(guiContainer, components);
    }

    public static ChestGui gui() {
        return new ChestGui();
    }

    public static PaginatedBuilder paginated() {
        return new PaginatedBuilder();
    }
}