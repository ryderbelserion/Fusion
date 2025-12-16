package com.ryderbelserion.fusion.kyori.permissions.objects;

import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.core.api.interfaces.permissions.IPermission;
import com.ryderbelserion.fusion.core.api.interfaces.permissions.enums.Mode;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;
import java.util.Map;

public class Permission implements IPermission {

    private final FusionKyori fusion = (FusionKyori) FusionProvider.getInstance();

    private final String node;
    private final String description;

    private final Mode permissionDefault;

    private final Map<String, Boolean> children;

    public Permission(@NotNull final String node, @NotNull final String description, @NotNull final Mode mode, @NotNull final Map<String, Boolean> children) {
        this.node = node;
        this.description = description;
        this.permissionDefault = mode;
        this.children = children;
    }

    @Override
    public void init() {
        this.fusion.registerPermission(this.permissionDefault, this.node, this.description, this.children);
    }

    @Override
    public void stop() {
        this.fusion.unregisterPermission(this.node);
    }

    @Override
    public boolean hasPermission(@NotNull final String node, @NotNull final Audience audience) {
        return this.fusion.hasPermission(audience, node);
    }

    @Override
    public boolean hasPermission(@NotNull final Audience audience) {
        return hasPermission(this.node, audience);
    }

    @Override
    public @NotNull final String getNode() {
        return this.node;
    }

    @Override
    public int getChildren() {
        return this.children.size();
    }
}