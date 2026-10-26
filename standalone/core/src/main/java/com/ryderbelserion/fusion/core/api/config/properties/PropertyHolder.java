package com.ryderbelserion.fusion.core.api.config.properties;

import com.ryderbelserion.fusion.core.api.config.annotations.Alias;
import org.jspecify.annotations.NullMarked;
import com.ryderbelserion.fusion.core.api.config.annotations.Comment;
import com.ryderbelserion.fusion.core.api.config.annotations.Disabled;
import com.ryderbelserion.fusion.core.api.config.properties.objects.interfaces.Property;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NullMarked
public final class PropertyHolder {

    private final Property<?> property;
    private final Object[] path;

    private final boolean hasComment;
    private List<String> comments = new ArrayList<>();

    private final boolean isDisabled;
    private String reason = "";

    private final boolean hasAlias;
    private String alias = "";

    public PropertyHolder(final Property<?> property, final Field field, final Object[] path) {
        this.isDisabled = field.isAnnotationPresent(Disabled.class);

        if (this.isDisabled) {
            this.reason = field.getDeclaredAnnotation(Disabled.class).value();
        }

        this.hasComment = field.isAnnotationPresent(Comment.class);

        if (this.hasComment) {
            this.comments = Arrays.asList(field.getDeclaredAnnotation(Comment.class).value());
        }

        this.hasAlias = field.isAnnotationPresent(Alias.class);

        if (this.hasAlias) {
            this.alias = field.getDeclaredAnnotation(Alias.class).value();
        }

        this.property = property;
        this.path = path;
    }

    public Property<?> getProperty() {
        return this.property;
    }

    public boolean hasComments() {
        return this.hasComment;
    }

    public List<String> getComments() {
        return this.comments;
    }

    public boolean isDisabled() {
        return this.isDisabled;
    }

    public String getReason() {
        return this.reason;
    }

    public String getAlias() {
        return this.alias;
    }

    public boolean hasAlias() {
        return this.hasAlias;
    }

    public Object[] getPath() {
        return this.path;
    }
}