package com.ryderbelserion.fusion.core.api.config.properties.interfaces;

import org.jspecify.annotations.NonNull;
import com.ryderbelserion.fusion.core.api.config.properties.builders.AliasBuilder;
import com.ryderbelserion.fusion.core.api.config.properties.builders.CommentsBuilder;

public interface IPropertyHolder {

    default void registerComments(@NonNull final CommentsBuilder builder) {

    }

    default void registerAliases(@NonNull final AliasBuilder builder) {

    }
}