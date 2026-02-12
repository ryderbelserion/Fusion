package com.ryderbelserion.fusion.kyori.permissions;

import com.ryderbelserion.fusion.kyori.permissions.enums.PermissionType;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PermissionContext {

    private final Map<String, Boolean> children = new HashMap<>();

    private final PermissionType type;
    private final String description;
    private final String permission;

    public PermissionContext(@NotNull final String permission, @NotNull final String description, @NotNull final PermissionType type) {
        this.permission = permission;
        this.description = description;
        this.type = type;
    }

    public PermissionContext(@NotNull final String permission, @NotNull final String description) {
        this(permission, description, PermissionType.OP);
    }

    public void addPermission(@NotNull final String permission, final boolean isChild) {
        this.children.put(permission, isChild);
    }

    public @NotNull final Map<String, Boolean> getChildren() {
        return Collections.unmodifiableMap(this.children);
    }

    public @NotNull final PermissionType getType() {
        return this.type;
    }

    public @NotNull final String getDescription() {
        return this.description;
    }

    public @NotNull final String getPermission() {
        return this.permission;
    }
}