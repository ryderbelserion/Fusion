package com.ryderbelserion.fusion.velocity;

import com.ryderbelserion.fusion.core.api.FusionKey;
import com.ryderbelserion.fusion.core.api.enums.Level;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import java.nio.file.Path;

public class FusionVelocity extends FusionKyori<Audience> {

    private final ComponentLogger logger;

    public FusionVelocity(@NotNull final ComponentLogger logger, @NotNull final Path path) {
        super(path);

        this.logger = logger;
    }

    @Override
    public void log(@NotNull final Level level, @NotNull final String message, @NotNull final Exception exception, @NotNull final Object... objects) {
        if (!this.isVerbose()) return;

        final Component component = asComponent(message.formatted(objects));

        switch (level) {
            case WARNING -> this.logger.warn(component, exception);
            case ERROR -> this.logger.error(component, exception);
            case INFO -> this.logger.info(component, exception);
        }
    }

    @Override
    public void log(@NotNull final Level level, @NotNull final String message, @NotNull final Object... objects) {
        if (!this.isVerbose()) return;

        final Component component = asComponent(message.formatted(objects));

        switch (level) {
            case WARNING -> this.logger.warn(component, objects);
            case ERROR -> this.logger.error(component, objects);
            case INFO -> this.logger.info(component, objects);
        }
    }

    @Override
    public final boolean isModReady(@NotNull final FusionKey key) {
        return false;
    }

    @Override
    public @NonNull final String getNamespace() {
        return "chatterbox";
    }

    @Override
    public @NotNull final String papi(@Nullable final Audience sender, @NotNull final String message) {
        return message;
    }
}