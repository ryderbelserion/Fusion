package com.ryderbelserion.fusion.core.api.registry.message.adapter;

import com.ryderbelserion.fusion.core.api.registry.message.adapter.interfaces.IMessageAdapter;
import com.ryderbelserion.fusion.core.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.BasicConfigurationNode;

public class JsonMessageAdapter extends IMessageAdapter<BasicConfigurationNode> {

    private final String value;

    public JsonMessageAdapter(@NotNull final BasicConfigurationNode configuration, @NotNull final String defaultValue, @NotNull final Object... path) {
        super(configuration, defaultValue, path);

        this.value = this.configuration.isList() ? StringUtils.toString(StringUtils.getStringList(this.configuration, defaultValue)) : this.configuration.getString(defaultValue);
    }

    @Override
    public @NotNull final String getValue() {
        return this.value;
    }
}