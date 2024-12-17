package com.ryderbelserion.paper.builder.gui;

import com.ryderbelserion.api.exception.FusionException;
import com.ryderbelserion.paper.builder.gui.enums.InteractionComponent;
import com.ryderbelserion.paper.builder.gui.types.BaseGui;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.Consumer;

public abstract class BaseGuiBuilder<G extends BaseGui, B extends BaseGuiBuilder<G, B>> {

    private final EnumSet<InteractionComponent> components = EnumSet.noneOf(InteractionComponent.class);
    private String title = null;
    private int rows = 1;

    private Consumer<G> consumer;

    public BaseGuiBuilder() {}

    public abstract @NotNull G create();

    public @NotNull final B setRows(final int rows) {
        this.rows = rows;

        return (B) this;
    }

    public @NotNull final B setTitle(@NotNull final String title) {
        this.title = title;

        return (B) this;
    }

    public final B disableItemPlacement() {
        this.components.add(InteractionComponent.PREVENT_ITEM_PLACE);

        return (B) this;
    }

    public final B disableItemTake() {
        this.components.add(InteractionComponent.PREVENT_ITEM_TAKE);

        return (B) this;
    }

    public final B disableItemSwap() {
        this.components.add(InteractionComponent.PREVENT_ITEM_SWAP);

        return (B) this;
    }

    public final B disableItemDrop() {
        this.components.add(InteractionComponent.PREVENT_ITEM_DROP);

        return (B) this;
    }

    public final B disableInteractions() {
        this.components.addAll(InteractionComponent.VALUES);

        return (B) this;
    }

    public final B enableInteractions() {
        this.components.clear();

        return (B) this;
    }

    public final B enableItemPlacement() {
        this.components.remove(InteractionComponent.PREVENT_ITEM_PLACE);

        return (B) this;
    }

    public final B enableItemTake() {
        this.components.remove(InteractionComponent.PREVENT_ITEM_TAKE);

        return (B) this;
    }

    public final B enableItemSwap() {
        this.components.remove(InteractionComponent.PREVENT_ITEM_SWAP);

        return (B) this;
    }

    public final B enableItemDrop() {
        this.components.remove(InteractionComponent.PREVENT_ITEM_DROP);

        return (B) this;
    }

    public @NotNull final B apply(@NotNull final Consumer<G> consumer) {
        this.consumer = consumer;

        return (B) this;
    }

    protected @NotNull final Set<InteractionComponent> getInteractionComponents() {
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

    protected final int getRows() {
        return this.rows;
    }
}