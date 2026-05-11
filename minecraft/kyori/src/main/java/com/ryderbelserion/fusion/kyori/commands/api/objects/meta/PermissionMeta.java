package com.ryderbelserion.fusion.kyori.commands.api.objects.meta;

import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import com.ryderbelserion.fusion.kyori.commands.CommandManager;
import com.ryderbelserion.fusion.kyori.commands.api.annotations.other.Permission;
import com.ryderbelserion.fusion.kyori.commands.api.enums.PermissionMode;
import com.ryderbelserion.fusion.kyori.commands.api.senders.objects.SenderExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PermissionMeta<S> {

    private final FusionKyori fusion = (FusionKyori) FusionProvider.getInstance();

    private final CommandManager commandManager = this.fusion.getCommandManager();

    private final PermissionMode mode;
    private final String description;
    private final String permission;

    public PermissionMeta(@Nullable final Permission permission) {
        this.description = permission != null ? permission.description() : "";
        this.permission = permission != null ? permission.permission() : "";
        this.mode = permission != null ? permission.mode() : PermissionMode.OP;
    }

    public boolean hasPermission(@NotNull final S context) {
        if (this.permission.isBlank()) {
            return true;
        }

        final SenderExtension<S> extension = this.commandManager.getSenderExtension();

        return extension.hasPermission(context, this.permission);
    }

    public void init() {
        if (this.permission.isBlank()) {
            return;
        }

        this.fusion.registerPermission(this.permission, this.description, this.mode);
    }
}