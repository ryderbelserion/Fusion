package com.ryderbelserion.fusion.core.api.builder;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.utils.AdvUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;

public class ComponentBuilder {

    private final FusionCore api = FusionCore.Provider.get();

    private final TextComponent.@NotNull Builder builder = Component.text();
    private final Audience target;
    private String value;

    public ComponentBuilder(@NotNull final Audience target) {
        this.target = target;
    }

    public @NotNull final ComponentBuilder append(@NotNull final Component component) {
        this.builder.append(component);

        return this;
    }

    public @NotNull final ComponentBuilder addHoverEvent(@NotNull final String text) {
        if (!text.isEmpty()) {
            this.builder.hoverEvent(HoverEvent.showText(AdvUtils.parse(text)));
        }

        return this;
    }

    public @NotNull final ComponentBuilder addClickEvent(@Nullable final ClickEvent.Action action, @NotNull final String text) {
        if (action != null && !text.isEmpty()) {
            this.builder.clickEvent(ClickEvent.clickEvent(action, text));
        }

        return this;
    }

    public @NotNull final TextComponent build() {
        if (this.value.isEmpty()) {
            return Component.empty();
        }

        return this.builder.append(this.api.color(this.target, this.value, new HashMap<>())).build();
    }

    public void send() {
        @NotNull final Component component = build();

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

    public @NotNull final String getValue() {
        return this.value;
    }
}