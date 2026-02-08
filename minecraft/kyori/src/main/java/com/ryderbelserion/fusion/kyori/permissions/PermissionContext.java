package com.ryderbelserion.fusion.kyori.permissions;

import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PermissionContext {

    private final Map<String, Boolean> children = new HashMap<>();

    private final String permission;
    private final String description;

    public PermissionContext(@NotNull final String permission, @NotNull final String description) {
        this.permission = permission;
        this.description = description;
    }

    public void addPermission(@NotNull final String permission, final boolean isChild) {
        this.children.put(permission, isChild);
    }

    public @NotNull final Map<String, Boolean> getChildren() {
        return Collections.unmodifiableMap(this.children);
    }

    public @NotNull final String getDescription() {
        return this.description;
    }

    public @NotNull final String getPermission() {
        return this.permission;
    }
}