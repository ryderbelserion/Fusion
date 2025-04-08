package com.ryderbelserion.fusion.paper.api.builder.gui.objects;

import com.ryderbelserion.fusion.paper.api.builder.gui.PaginatedBuilder;
import com.ryderbelserion.fusion.paper.api.builder.gui.ScrollingBuilder;
import com.ryderbelserion.fusion.paper.api.builder.gui.StorageBuilder;
import com.ryderbelserion.fusion.paper.api.builder.gui.TypedGuiBuilder;
import com.ryderbelserion.fusion.paper.api.builder.gui.enums.GuiScrollType;
import com.ryderbelserion.fusion.paper.api.builder.gui.enums.GuiType;
import com.ryderbelserion.fusion.paper.api.builder.gui.interfaces.GuiContainer;
import com.ryderbelserion.fusion.paper.api.builder.gui.types.ChestGui;
import com.ryderbelserion.fusion.paper.api.builder.gui.enums.GuiComponent;
import com.ryderbelserion.fusion.paper.api.builder.gui.types.BaseGui;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import java.util.Set;

public final class Gui extends BaseGui {

    public Gui(@NotNull final GuiContainer guiContainer, @NotNull final Set<GuiComponent> components) {
        super(guiContainer, components);
    }

    @Contract("_ -> new")
    public static @NotNull ScrollingBuilder scrolling(@NotNull final GuiScrollType scrollType) {
        return new ScrollingBuilder(scrollType);
    }

    @Contract("_ -> new")
    public static @NotNull TypedGuiBuilder gui(@NotNull final GuiType type) {
        return new TypedGuiBuilder(type);
    }

    @Contract(" -> new")
    public static @NotNull ScrollingBuilder scrolling() {
        return scrolling(GuiScrollType.VERTICAL);
    }
    
    @Contract(" -> new")
    public static @NotNull StorageBuilder storage() {
        return new StorageBuilder();
    }

    @Contract(" -> new")
    public static PaginatedBuilder paginated() {
        return new PaginatedBuilder();
    }

    @Contract(" -> new")
    public static ChestGui gui() {
        return new ChestGui();
    }
}