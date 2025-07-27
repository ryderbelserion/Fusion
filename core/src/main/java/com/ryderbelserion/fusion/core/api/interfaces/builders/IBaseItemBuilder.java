package com.ryderbelserion.fusion.core.api.interfaces.builders;

import com.ryderbelserion.fusion.core.api.enums.ItemState;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

public abstract class IBaseItemBuilder<B, I, T> {

    protected Map<String, String> placeholders = new HashMap<>();
    protected I itemStack;
    protected T itemType;

    public IBaseItemBuilder(@NotNull final I itemStack) {
        this.itemStack = itemStack;
    }

    public IBaseItemBuilder(@NotNull final String itemStack) {
        withCustomItem(itemStack);
    }

    public IBaseItemBuilder() {}

    public abstract B withCustomItem(@NotNull final String itemStack);

    public abstract B withBase64(@NotNull final String itemStack);

    public abstract B withType(@NotNull final T itemType, final int amount);

    public abstract B withType(@NotNull final T itemType);

    public @NotNull B addPlaceholder(@NotNull final String placeholder, @NotNull final String value) {
        this.placeholders.put(placeholder, value);

        return (B) this;
    }

    public @NotNull B setPlaceholders(@NotNull final Map<String, String> placeholders) {
        this.placeholders = placeholders;

        return (B) this;
    }

    public @NotNull B removePlaceholder(@NotNull final String placeholder) {
        this.placeholders.remove(placeholder);

        return (B) this;
    }

    public boolean hasPlaceholder(@NotNull final String placeholder) {
        return this.placeholders.containsKey(placeholder);
    }
}