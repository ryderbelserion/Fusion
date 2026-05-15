package com.ryderbelserion.fusion.core.api.registry.message.adapter.interfaces;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.FusionProvider;
import org.jspecify.annotations.NonNull;
import org.spongepowered.configurate.ScopedConfigurationNode;

public abstract class IMessageAdapter<N extends ScopedConfigurationNode<N>> {

    protected final FusionCore fusion = FusionProvider.getInstance();

    protected final N configuration;
    protected final String defaultValue;
    protected final Object[] path;

    public IMessageAdapter(@NonNull final N configuration, @NonNull final String defaultValue, @NonNull final Object... path) {
        this.configuration = configuration.node(path);
        this.defaultValue = defaultValue;
        this.path = path;
    }

    public abstract @NonNull String getValue();
}