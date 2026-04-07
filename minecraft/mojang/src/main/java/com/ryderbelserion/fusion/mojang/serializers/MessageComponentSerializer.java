package com.ryderbelserion.fusion.mojang.serializers;

import com.mojang.brigadier.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import java.util.Optional;
import java.util.ServiceLoader;

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