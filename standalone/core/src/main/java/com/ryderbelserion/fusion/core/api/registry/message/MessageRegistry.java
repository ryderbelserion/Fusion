package com.ryderbelserion.fusion.core.api.registry.message;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.FusionKey;
import com.ryderbelserion.fusion.core.api.enums.Level;
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
    private final FusionCore fusion;

    public MessageRegistry(@NonNull final FusionCore fusion, @NonNull final FusionKey defaultKey) {
        this.defaultKey = defaultKey;
        this.fusion = fusion;
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
        this.fusion.log(Level.INFO, "Registering the message @ %s for %s".formatted(key.asString(), message.asString()));

        this.messages.computeIfAbsent(key, _ -> new HashMap<>()).put(message, adapter);
    }

    public void removeKey(@NonNull final FusionKey key, @NonNull final FusionKey message) {
        this.messages.computeIfPresent(key, (_, map) -> {
            map.remove(message);

            return map;
        });
    }

    public void addKey(@NonNull final FusionKey message, @NonNull final IMessageAdapter adapter) {
        addKey(this.defaultKey, message, adapter);
    }

    public void removeKey(@NonNull final FusionKey message) {
        removeKey(this.defaultKey, message);
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