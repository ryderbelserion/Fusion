package com.ryderbelserion.fusion.core.api.registry.message;

import com.ryderbelserion.fusion.core.api.FusionKey;
import com.ryderbelserion.fusion.core.api.registry.message.adapter.interfaces.IMessageAdapter;
import org.jspecify.annotations.NonNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class MessageRegistry {

    private final Map<FusionKey, Map<FusionKey, IMessageAdapter>> messages = new HashMap<>();

    private final FusionKey defaultKey;

    public MessageRegistry(@NonNull final FusionKey defaultKey) {
        this.defaultKey = defaultKey;
    }

    public void init(@NonNull final Consumer<MessageRegistry> consumer) {
        this.messages.clear();

        this.messages.put(this.defaultKey, new HashMap<>());

        consumer.accept(this);
    }

    public void init() {
        init(_ -> {});
    }

    public void addKey(@NonNull final FusionKey key, @NonNull final FusionKey message, @NonNull final IMessageAdapter adapter) {
        this.messages.getOrDefault(key, new HashMap<>()).put(message, adapter);
    }

    public void removeKey(@NonNull final FusionKey key, @NonNull final FusionKey message) {
        this.messages.getOrDefault(key, new HashMap<>()).remove(message);
    }

    public @NonNull Optional<IMessageAdapter> getMessageByLocale(@NonNull final FusionKey key, @NonNull final FusionKey message) {
        return Optional.ofNullable(this.messages.getOrDefault(key, this.messages.get(this.defaultKey)).get(message));
    }

    public @NonNull Optional<IMessageAdapter> getMessage(@NonNull final FusionKey message) {
        return Optional.ofNullable(this.messages.get(this.defaultKey).get(message));
    }

    public @NonNull Map<FusionKey, Map<FusionKey, IMessageAdapter>> getMessages() {
        return Collections.unmodifiableMap(this.messages);
    }

    public @NonNull FusionKey getDefaultKey() {
        return this.defaultKey;
    }
}