package com.ryderbelserion.fusion.core.api.registry.message.adapter;

import com.ryderbelserion.fusion.core.api.registry.message.adapter.interfaces.IMessageAdapter;
import com.ryderbelserion.fusion.core.utils.StringUtils;
import org.jspecify.annotations.NonNull;
import org.spongepowered.configurate.BasicConfigurationNode;
import java.util.HashMap;
import java.util.Map;

public class JsonMessageAdapter extends IMessageAdapter<BasicConfigurationNode> {

    private final String value;

    public JsonMessageAdapter(@NonNull final BasicConfigurationNode configuration, @NonNull final String defaultValue, @NonNull final Map<String, String> placeholders, @NonNull final Object... path) {
        super(configuration, defaultValue, path);

        this.value = this.fusion.replacePlaceholders(this.configuration.isList() ? StringUtils.toString(StringUtils.getStringList(this.configuration, defaultValue)) : this.configuration.getString(defaultValue), placeholders);
    }

    public JsonMessageAdapter(@NonNull final BasicConfigurationNode configuration, @NonNull final String defaultValue, @NonNull final Object... path) {
        this(configuration, defaultValue, new HashMap<>(), path);
    }

    @Override
    public @NonNull final String getValue() {
        return this.value;
    }
}