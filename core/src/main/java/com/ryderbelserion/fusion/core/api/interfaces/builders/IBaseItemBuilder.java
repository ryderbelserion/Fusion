package com.ryderbelserion.fusion.core.api.interfaces.builders;

import org.jetbrains.annotations.NotNull;

public abstract class IBaseItemBuilder<B, I, T> {

    protected I itemStack;
    protected T itemType;

    public IBaseItemBuilder(@NotNull final I itemStack) {
        this.itemStack = itemStack;
    }

    public IBaseItemBuilder(@NotNull final String itemStack) {
        withCustomItem(itemStack);
    }

    public abstract B withCustomItem(@NotNull final String itemStack);

    public abstract B withBase64(@NotNull final String itemStack);

    public abstract B withType(@NotNull final T itemType, final int amount);

    public abstract B withType(@NotNull final T itemType);
}