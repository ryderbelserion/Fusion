package com.ryderbelserion.fusion.kyori.commands.api.senders.objects;

import com.ryderbelserion.fusion.kyori.commands.api.objects.BasicCommand;
import com.ryderbelserion.fusion.kyori.commands.api.senders.results.ValidationResult;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;
import java.util.Set;

public interface SenderExtension<S> extends SenderMapper<S> {

    @NotNull ValidationResult<String> validate(@NotNull final BasicCommand<S> command, @NotNull final Class<?> target, @NotNull final S sender);

    boolean hasPermission(@NotNull final S source, @NotNull final String permission);

    @NotNull Audience getAudience(@NotNull final S sender);

    void sendMessage(@NotNull final S sender, @NotNull final String message);

    @NotNull Set<Class<?>> getSenders();

    interface Default<S> extends SenderExtension<S> {

        @Override
        default @NotNull ValidationResult<String> validate(
                @NotNull final BasicCommand<S> command,
                @NotNull final Class<?> target,
                @NotNull final S sender
        ) {
            return valid();
        }

        @Override
        default @NotNull S map(@NotNull final S defaultSender) {
            return defaultSender;
        }
    }

    default ValidationResult<String> invalid(@NotNull final String message) {
        return new ValidationResult.Invalid<>(message);
    }

    default ValidationResult<String> valid() {
        return new ValidationResult.Valid<>();
    }
}