package com.ryderbelserion.fusion.config.keys;

import com.ryderbelserion.fusion.config.enums.TestEnum;
import com.ryderbelserion.fusion.core.api.config.annotations.Alias;
import com.ryderbelserion.fusion.core.api.config.annotations.Comment;
import com.ryderbelserion.fusion.core.api.config.properties.builders.AliasBuilder;
import com.ryderbelserion.fusion.core.api.config.properties.builders.CommentsBuilder;
import com.ryderbelserion.fusion.core.api.config.properties.interfaces.IPropertyHolder;
import com.ryderbelserion.fusion.core.api.config.properties.objects.EnumProperty;
import com.ryderbelserion.fusion.core.api.config.properties.objects.interfaces.Property;
import org.jspecify.annotations.NullMarked;

import static com.ryderbelserion.fusion.core.api.config.properties.PropertyBuilder.newBeanProperty;
import static com.ryderbelserion.fusion.core.api.config.properties.PropertyBuilder.newEnumProperty;
import static com.ryderbelserion.fusion.core.api.config.properties.PropertyBuilder.newProperty;

@NullMarked
public final class ConfigKeys implements IPropertyHolder {

    @Override
    public void registerComments(final CommentsBuilder builder) {
        builder.setComment("This is a comment", "test", "section");

        builder.setComment("this is another comment", "test", "other-section");
    }

    @Override
    public void registerAliases(final AliasBuilder builder) {
        builder.addAlias("test", "test", "section");

        builder.addAlias("test_two", "test", "other-section");
    }

    @Comment("This is a test!")
    @Alias("test")
    public static final Property<Boolean> test_boolean = newProperty(true, "toggle");

    @Comment("This is a test x2!")
    @Alias("test")
    public static final Property<Boolean> test_boolean_two = newProperty(false, "other-toggle");

    @Comment("""
            This is an enum config option
            This is a multi line comment
            """)
    @Alias("test_two")
    public static final EnumProperty<TestEnum> test_enum = newEnumProperty(TestEnum.class, TestEnum.example_one, "test-enum");
}