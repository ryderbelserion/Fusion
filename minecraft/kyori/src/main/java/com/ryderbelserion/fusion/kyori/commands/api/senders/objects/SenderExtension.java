package com.ryderbelserion.fusion.kyori.commands.api.senders.objects;

import com.ryderbelserion.fusion.kyori.commands.api.senders.results.ValidationResult;
import org.jetbrains.annotations.NotNull;
import java.util.Set;

public interface SenderExtension<S> extends SenderMapper<S> {

    @NotNull ValidationResult<String> validate(@NotNull final Class<?> target, @NotNull final S sender);

    boolean hasPermission(@NotNull final S source, @NotNull final String permission);

    void sendMessage(@NotNull final S sender, @NotNull final String message);

    @NotNull Class<? extends S> getSenderType(@NotNull final Class<?> klass);

    @NotNull Set<Class<?>> getSenders();

    interface Default<S> extends SenderExtension<S> {

        @Override
        default @NotNull ValidationResult<String> validate(
                @NotNull final Class<?> target,
                @NotNull final S sender
        ) {
            return valid();
        }

        @Override
        default @NotNull Object map(@NotNull final Class<?> type, @NotNull final S defaultSender) {
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