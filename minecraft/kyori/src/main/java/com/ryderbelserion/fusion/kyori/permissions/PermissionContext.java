package com.ryderbelserion.fusion.kyori.permissions;

import com.ryderbelserion.fusion.kyori.permissions.enums.PermissionType;
import org.jspecify.annotations.NullMarked;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@NullMarked
public class PermissionContext {

    private final Map<String, Boolean> children = new HashMap<>();

    private final PermissionType type;
    private final String description;
    private final String permission;

    public PermissionContext(final String permission, final String description, final PermissionType type) {
        this.permission = permission;
        this.description = description;
        this.type = type;
    }

    public PermissionContext(final String permission, final String description) {
        this(permission, description, PermissionType.OP);
    }

    public void addPermission(final String permission, final boolean isChild) {
        this.children.put(permission, isChild);
    }

    public final Map<String, Boolean> getChildren() {
        return Collections.unmodifiableMap(this.children);
    }

    public final PermissionType getType() {
        return this.type;
    }

    public final String getDescription() {
        return this.description;
    }

    public final String getPermission() {
        return this.permission;
    }
}