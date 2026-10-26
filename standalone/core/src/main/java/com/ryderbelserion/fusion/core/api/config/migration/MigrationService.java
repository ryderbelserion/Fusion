package com.ryderbelserion.fusion.core.api.config.migration;

import com.ryderbelserion.fusion.core.api.config.properties.interfaces.IPropertyData;
import org.spongepowered.configurate.CommentedConfigurationNode;

public interface MigrationService {

    // true = required
    // false = not required
    boolean copy(final CommentedConfigurationNode configuration, final IPropertyData property);

}