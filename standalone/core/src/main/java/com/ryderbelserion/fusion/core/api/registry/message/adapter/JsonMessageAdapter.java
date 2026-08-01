package com.ryderbelserion.fusion.core.api.registry.message.adapter;

import com.ryderbelserion.fusion.core.api.registry.message.adapter.interfaces.IMessageAdapter;
import com.ryderbelserion.fusion.core.utils.StringUtils;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.BasicConfigurationNode;
import java.util.HashMap;
import java.util.Map;

@NullMarked
public class JsonMessageAdapter extends IMessageAdapter<BasicConfigurationNode> {

    private final String value;

    public JsonMessageAdapter(final BasicConfigurationNode configuration, final String defaultValue, final Map<String, String> placeholders, final Object... path) {
        super(configuration, defaultValue, path);

        this.value = this.fusion.replacePlaceholders(this.configuration.isList() ? StringUtils.toString(StringUtils.getStringList(this.configuration, defaultValue)) : this.configuration.getString(defaultValue), placeholders);
    }

    public JsonMessageAdapter(final BasicConfigurationNode configuration, final String defaultValue, final Object... path) {
        this(configuration, defaultValue, new HashMap<>(), path);
    }

    @Override
    public String getValue(final Object object) {
        return this.fusion.papi(object, this.value);
    }

    @Override
    public final String getValue() {
        return this.value;
    }
}