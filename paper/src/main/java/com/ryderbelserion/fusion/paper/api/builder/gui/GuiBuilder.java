package com.ryderbelserion.fusion.paper.api.builder.gui;

import com.ryderbelserion.fusion.api.exceptions.FusionException;
import com.ryderbelserion.fusion.paper.api.builder.gui.enums.GuiComponent;
import com.ryderbelserion.fusion.paper.api.builder.gui.types.BaseGui;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.Consumer;

public abstract class GuiBuilder<G extends BaseGui, B extends GuiBuilder<G, B>> {

    private final EnumSet<GuiComponent> components = EnumSet.noneOf(GuiComponent.class);
    private String title = null;

    private Consumer<G> consumer;

    public GuiBuilder() {}

    public abstract @NotNull G create();

    public @NotNull final B setTitle(@NotNull final String title) {
        this.title = title;

        return (B) this;
    }

    public final B disableItemPlacement() {
        this.components.add(GuiComponent.PREVENT_ITEM_PLACE);

        return (B) this;
    }

    public final B disableItemTake() {
        this.components.add(GuiComponent.PREVENT_ITEM_TAKE);

        return (B) this;
    }

    public final B disableItemSwap() {
        this.components.add(GuiComponent.PREVENT_ITEM_SWAP);

        return (B) this;
    }

    public final B disableItemDrop() {
        this.components.add(GuiComponent.PREVENT_ITEM_DROP);

        return (B) this;
    }

    public final B disableInteractions() {
        this.components.addAll(GuiComponent.VALUES);

        return (B) this;
    }

    public final B enableInteractions() {
        this.components.clear();

        return (B) this;
    }

    public final B enableItemPlacement() {
        this.components.remove(GuiComponent.PREVENT_ITEM_PLACE);

        return (B) this;
    }

    public final B enableItemTake() {
        this.components.remove(GuiComponent.PREVENT_ITEM_TAKE);

        return (B) this;
    }

    public final B enableItemSwap() {
        this.components.remove(GuiComponent.PREVENT_ITEM_SWAP);

        return (B) this;
    }

    public final B enableItemDrop() {
        this.components.remove(GuiComponent.PREVENT_ITEM_DROP);

        return (B) this;
    }

    public @NotNull final B apply(@NotNull final Consumer<G> consumer) {
        this.consumer = consumer;

        return (B) this;
    }

    protected @NotNull final Set<GuiComponent> getComponents() {
        return this.components;
    }

    protected @Nullable final Consumer<G> getConsumer() {
        return this.consumer;
    }

    protected @NotNull final String getTitle() {
        if (this.title == null) {
            throw new FusionException("The gui title is missing!");
        }

        return this.title;
    }

    protected void consumeBuilder(@NotNull final BaseGuiBuilder<?, ?> builder) {
        this.title = builder.getTitle();

        this.components.clear();
        this.components.addAll(builder.getComponents());
    }
}