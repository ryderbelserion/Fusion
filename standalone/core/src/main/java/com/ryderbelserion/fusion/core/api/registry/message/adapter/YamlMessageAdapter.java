package com.ryderbelserion.fusion.core.api.registry.message.adapter;

import com.ryderbelserion.fusion.core.api.registry.message.adapter.interfaces.IMessageAdapter;
import com.ryderbelserion.fusion.core.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.HashMap;
import java.util.Map;

public class YamlMessageAdapter extends IMessageAdapter<CommentedConfigurationNode> {

    private final String value;

    public YamlMessageAdapter(@NotNull final CommentedConfigurationNode configuration, @NotNull final String defaultValue, @NotNull final Map<String, String> placeholders, @NotNull final Object... path) {
        super(configuration, defaultValue, path);

        this.value = this.fusion.replacePlaceholders(this.configuration.isList() ? StringUtils.toString(StringUtils.getStringList(this.configuration, defaultValue)) : this.configuration.getString(defaultValue), placeholders);
    }

    public YamlMessageAdapter(@NotNull final CommentedConfigurationNode configuration, @NotNull final String defaultValue, @NotNull final Object... path) {
        this(configuration, defaultValue, new HashMap<>(), path);
    }

    @Override
    public @NotNull final String getValue() {
        return this.value;
    }
}