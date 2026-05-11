package com.ryderbelserion.fusion.kyori.commands.api.senders.objects;

import com.ryderbelserion.fusion.kyori.commands.api.senders.results.ValidationResult;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;
import java.util.Set;

public interface SenderExtension<S> {

    @NotNull ValidationResult<?> validate(final @NotNull Class<?> target, final @NotNull S sender);

    @NotNull Audience getAudience(@NotNull final S sender);

    @NotNull Set<Class<?>> getSenders();

    default ValidationResult<?> invalid(String message) {
        return new ValidationResult.Invalid<>(message);
    }

    default ValidationResult<?> valid() {
        return new ValidationResult.Valid<>();
    }
}