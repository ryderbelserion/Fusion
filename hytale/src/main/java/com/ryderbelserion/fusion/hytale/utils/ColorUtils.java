package com.ryderbelserion.fusion.hytale.utils;

import com.hypixel.hytale.server.core.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jspecify.annotations.NonNull;

public class ColorUtils {

    /**
     * Converts a component to a hytale message
     *
     * @param component {@link Component}
     * @author lucko
     * @return {@link Message}
     */
    public static Message toHytale(@NonNull final Component component) {
        if (!(component instanceof TextComponent text)) {
            throw new UnsupportedOperationException("Unsupported component type: " + component.getClass());
        }

        final Message message = Message.raw(text.content());

        final TextColor color = text.color();

        if (color != null) {
            message.color(color.asHexString());
        }

        final TextDecoration.State bold = text.decoration(TextDecoration.BOLD);

        if (bold != TextDecoration.State.NOT_SET) {
            message.bold(bold == TextDecoration.State.TRUE);
        }

        final TextDecoration.State italic = text.decoration(TextDecoration.ITALIC);

        if (italic != TextDecoration.State.NOT_SET) {
            message.italic(italic == TextDecoration.State.TRUE);
        }

        final ClickEvent<?> clickEvent = text.clickEvent();

        if (clickEvent != null && clickEvent.action() == ClickEvent.Action.OPEN_URL) {
            final ClickEvent.Payload payload = clickEvent.payload();

            if (payload instanceof ClickEvent.Payload.Text value) {
                message.link(value.value());
            }
        }

        message.insertAll(text.children().stream()
                .map(ColorUtils::toHytale)
                .toList()
        );

        return message;
    }
}