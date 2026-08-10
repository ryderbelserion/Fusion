package com.ryderbelserion.fusion.kyori.commands.serializers;

import com.mojang.brigadier.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.jspecify.annotations.NullMarked;
import java.util.Optional;
import java.util.ServiceLoader;

@NullMarked
public interface MessageComponentSerializer extends ComponentSerializer<Component, Component, Message> {

    /**
     * A component serializer for converting between {@link Message} and {@link Component}.
     *
     * @return serializer instance
     */
    static MessageComponentSerializer message() {
        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")

        final class Holder {
            static final Optional<MessageComponentSerializer> PROVIDER = ServiceLoader.load(MessageComponentSerializer.class, MessageComponentSerializer.class.getClassLoader()).findFirst();
        }

        return Holder.PROVIDER.orElseThrow();
    }
}