package com.ryderbelserion.fusion.kyori.permissions;

import com.ryderbelserion.fusion.kyori.permissions.enums.PermissionType;
import org.jspecify.annotations.NonNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PermissionContext {

    private final Map<String, Boolean> children = new HashMap<>();

    private final PermissionType type;
    private final String description;
    private final String permission;

    public PermissionContext(@NonNull final String permission, @NonNull final String description, @NonNull final PermissionType type) {
        this.permission = permission;
        this.description = description;
        this.type = type;
    }

    public PermissionContext(@NonNull final String permission, @NonNull final String description) {
        this(permission, description, PermissionType.OP);
    }

    public void addPermission(@NonNull final String permission, final boolean isChild) {
        this.children.put(permission, isChild);
    }

    public @NonNull final Map<String, Boolean> getChildren() {
        return Collections.unmodifiableMap(this.children);
    }

    public @NonNull final PermissionType getType() {
        return this.type;
    }

    public @NonNull final String getDescription() {
        return this.description;
    }

    public @NonNull final String getPermission() {
        return this.permission;
    }
}