package com.ryderbelserion.fusion.core.api.registry.message.adapter.interfaces;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.FusionProvider;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.ScopedConfigurationNode;

@NullMarked
public abstract class IMessageAdapter<N extends ScopedConfigurationNode<N>> {

    protected final FusionCore fusion = FusionProvider.getInstance();

    protected final String defaultValue;
    protected final N configuration;
    protected final Object[] path;

    public IMessageAdapter(final N configuration, final String defaultValue, final Object... path) {
        this.configuration = configuration.node(path);
        this.defaultValue = defaultValue;
        this.path = path;
    }

    public abstract String getValue(final Object object);

    public abstract String getValue();
}