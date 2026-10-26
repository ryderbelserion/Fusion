package com.ryderbelserion.fusion.config.keys;

import com.ryderbelserion.fusion.core.api.config.annotations.Alias;
import com.ryderbelserion.fusion.core.api.config.annotations.Comment;
import com.ryderbelserion.fusion.core.api.config.properties.builders.AliasBuilder;
import com.ryderbelserion.fusion.core.api.config.properties.builders.CommentsBuilder;
import com.ryderbelserion.fusion.core.api.config.properties.interfaces.IPropertyHolder;
import com.ryderbelserion.fusion.core.api.config.properties.objects.interfaces.Property;
import org.jspecify.annotations.NullMarked;
import static com.ryderbelserion.fusion.core.api.config.properties.PropertyBuilder.newProperty;

@NullMarked
public final class ConfigKeys implements IPropertyHolder {

    @Override
    public void registerComments(final CommentsBuilder builder) {
        builder.setComment("This is a comment", "test", "section");
    }

    @Override
    public void registerAliases(final AliasBuilder builder) {
        builder.addAlias("test", "test", "section");
    }

    @Comment("This is a test!")
    @Alias("test")
    public static final Property<Boolean> test_boolean = newProperty(true, "toggle");

    @Comment("This is a test x2!")
    @Alias("test")
    public static final Property<Boolean> test_boolean_two = newProperty(false, "other-toggle");
}