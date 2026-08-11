package com.ryderbelserion.fusion.core.api.registry.message;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.api.objects.FusionKey;
import com.ryderbelserion.fusion.api.enums.Level;
import com.ryderbelserion.fusion.core.api.registry.message.adapter.interfaces.IMessageAdapter;
import org.jspecify.annotations.NullMarked;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@NullMarked
public final class MessageRegistry {

    private final Map<FusionKey, Map<FusionKey, IMessageAdapter>> messages = new HashMap<>();

    private final FusionKey defaultKey;
    private final FusionCore fusion;

    public MessageRegistry(final FusionCore fusion, final FusionKey defaultKey) {
        this.defaultKey = defaultKey;
        this.fusion = fusion;
    }

    public void init(final Consumer<MessageRegistry> consumer) {
        this.messages.clear();

        this.messages.put(this.defaultKey, new HashMap<>());

        consumer.accept(this);
    }

    public void init() {
        init(_ -> {});
    }

    public void addKey(final FusionKey key, final FusionKey message, final IMessageAdapter adapter) {
        this.fusion.log(Level.info, "Registering the message @ %s for %s".formatted(key.asString(), message.asString()));

        this.messages.computeIfAbsent(key, _ -> new HashMap<>()).put(message, adapter);
    }

    public void removeKey(final FusionKey key, final FusionKey message) {
        this.messages.computeIfPresent(key, (_, map) -> {
            map.remove(message);

            return map;
        });
    }

    public void addKey(final FusionKey message, final IMessageAdapter adapter) {
        addKey(this.defaultKey, message, adapter);
    }

    public void removeKey(final FusionKey message) {
        removeKey(this.defaultKey, message);
    }

    public Optional<IMessageAdapter> getMessageByLocale(final FusionKey key, final FusionKey message) {
        return Optional.ofNullable(this.messages.getOrDefault(key, this.messages.get(this.defaultKey)).get(message));
    }

    public Optional<IMessageAdapter> getMessage(final FusionKey message) {
        return Optional.ofNullable(this.messages.get(this.defaultKey).get(message));
    }

    public Map<FusionKey, Map<FusionKey, IMessageAdapter>> getMessages() {
        return Collections.unmodifiableMap(this.messages);
    }

    public FusionKey getDefaultKey() {
        return this.defaultKey;
    }
}