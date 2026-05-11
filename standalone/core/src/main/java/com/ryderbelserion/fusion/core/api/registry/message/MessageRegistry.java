package com.ryderbelserion.fusion.core.api.registry.message;

import com.ryderbelserion.fusion.core.api.FusionKey;
import com.ryderbelserion.fusion.core.api.registry.message.adapter.interfaces.IMessageAdapter;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class MessageRegistry {

    private final Map<FusionKey, Map<FusionKey, IMessageAdapter>> messages = new HashMap<>();

    private FusionKey defaultKey;

    public void init(@NotNull final FusionKey defaultKey, @NotNull final Consumer<MessageRegistry> consumer) {
        this.messages.clear();

        this.messages.put(this.defaultKey = defaultKey, new HashMap<>());

        consumer.accept(this);
    }

    public void addKey(@NotNull final FusionKey key, @NotNull final FusionKey message, @NotNull final IMessageAdapter adapter) {
        final Map<FusionKey, IMessageAdapter> keys = this.messages.getOrDefault(key, new HashMap<>());

        keys.put(message, adapter);
    }

    public void removeKey(@NotNull final FusionKey key, @NotNull final FusionKey message) {
        final Map<FusionKey, IMessageAdapter> keys = this.messages.getOrDefault(key, new HashMap<>());

        keys.remove(message);
    }

    public @NotNull Optional<IMessageAdapter> getMessageByLocale(@NotNull final FusionKey key, @NotNull final FusionKey message) {
        final Map<FusionKey, IMessageAdapter> keys = this.messages.getOrDefault(key, this.messages.get(this.defaultKey));

        return Optional.ofNullable(keys.get(message));
    }

    public @NotNull Optional<IMessageAdapter> getMessage(@NotNull final FusionKey message) {
        return Optional.ofNullable(this.messages.get(this.defaultKey).get(message));
    }
}