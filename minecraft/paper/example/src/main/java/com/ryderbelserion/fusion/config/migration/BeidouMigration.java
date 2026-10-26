package com.ryderbelserion.fusion.config.migration;

import com.ryderbelserion.fusion.config.migration.enums.PropertyStorage;
import com.ryderbelserion.fusion.core.api.config.migration.MigrationService;
import com.ryderbelserion.fusion.core.api.config.properties.interfaces.IPropertyData;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.CommentedConfigurationNode;

@NullMarked
public class BeidouMigration implements MigrationService {

    @Override
    public boolean copy(final CommentedConfigurationNode configuration, final IPropertyData property) {
        return PropertyStorage.test_boolean.copy(configuration, property);
    }
}