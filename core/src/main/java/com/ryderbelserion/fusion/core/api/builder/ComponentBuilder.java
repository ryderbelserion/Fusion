package com.ryderbelserion.core.api.builder;

import com.ryderbelserion.core.FusionLayout;
import com.ryderbelserion.core.FusionProvider;
import com.ryderbelserion.core.util.StringUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;

public class ComponentBuilder {

    private final FusionLayout api = FusionProvider.get();

    private final TextComponent.@NotNull Builder builder = Component.text();
    private final Audience target;
    private String value;

    public ComponentBuilder(@NotNull final Audience target) {
        this.target = target;
    }

    public @NotNull ComponentBuilder append(@NotNull final Component component) {
        this.builder.append(component);

        return this;
    }

    public @NotNull ComponentBuilder addHoverEvent(@NotNull final String text) {
        if (!text.isEmpty()) {
            this.builder.hoverEvent(HoverEvent.showText(StringUtils.parse(text)));
        }

        return this;
    }

    public @NotNull ComponentBuilder addClickEvent(@Nullable final ClickEvent.Action action, @NotNull final String text) {
        if (action != null && !text.isEmpty()) {
            this.builder.clickEvent(ClickEvent.clickEvent(action, text));
        }

        return this;
    }

    public @NotNull TextComponent build() {
        if (this.value.isEmpty()) {
            return Component.empty();
        }

        return this.builder.append(this.api.color(this.target, this.value, new HashMap<>())).build();
    }

    public void send() {
        final Component component = build();

        if (!component.equals(Component.empty())) {
            this.target.sendMessage(component);
        }
    }

    public @NotNull Audience getTarget() {
        return this.target;
    }

    public void setValue(@NotNull final String value) {
        if (!value.isEmpty()) {
            this.value = value;
        }
    }

    public @NotNull String getValue() {
        return this.value;
    }
}