package com.ryderbelserion.fusion.kyori.commands.api.objects.meta;

import com.ryderbelserion.fusion.core.api.FusionKey;
import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.core.api.registry.message.MessageRegistry;
import com.ryderbelserion.fusion.core.api.registry.message.adapter.interfaces.IMessageAdapter;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import com.ryderbelserion.fusion.kyori.commands.CommandManager;
import com.ryderbelserion.fusion.kyori.commands.api.annotations.other.Permission;
import com.ryderbelserion.fusion.kyori.commands.api.enums.PermissionMode;
import com.ryderbelserion.fusion.kyori.commands.api.senders.objects.SenderExtension;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Optional;

public class PermissionMeta<S> {

    private final FusionKyori fusion = (FusionKyori) FusionProvider.getInstance();

    private final CommandManager commandManager = this.fusion.getCommandManager();

    private final MessageRegistry registry = this.fusion.getMessageRegistry();

    private final PermissionMode mode;
    private final String description;
    private final String permission;
    private final String message;

    public PermissionMeta(@Nullable final Permission permission) {
        this.description = permission != null ? permission.description() : "";
        this.permission = permission != null ? permission.permission() : "";
        this.message = permission != null ? permission.message() : "";
        this.mode = permission != null ? permission.mode() : PermissionMode.OP;
    }

    public @NotNull Optional<IMessageAdapter> getAdapterByLocale(@NotNull final FusionKey key, @NotNull final String namespace) {
        return this.registry.getMessageByLocale(key, FusionKey.key(namespace, this.message));
    }

    public @NotNull Optional<IMessageAdapter> getAdapter(@NotNull final String namespace) {
        return this.registry.getMessage(FusionKey.key(namespace, this.message));
    }

    public boolean hasPermission(@NotNull final S context) {
        if (this.permission.isBlank()) {
            return true;
        }

        if (this.fusion.hasPermission(context, this.permission)) {
            return true;
        }

        final SenderExtension<S> extension = this.commandManager.getSenderExtension();

        final Audience audience = extension.getAudience(context);

        final LocaleMeta meta = new LocaleMeta(audience);

        getAdapterByLocale(meta.getLocale(), this.fusion.getNamespace()).ifPresent(adapter -> audience.sendMessage(this.fusion.asComponent(audience, adapter.getValue())));

        return false;
    }

    public void init() {
        if (this.permission.isBlank()) {
            return;
        }

        this.fusion.registerPermission(this.permission, this.description, this.mode);
    }
}