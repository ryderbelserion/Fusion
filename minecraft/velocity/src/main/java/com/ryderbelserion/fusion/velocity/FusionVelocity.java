package com.ryderbelserion.fusion.velocity;

import com.ryderbelserion.fusion.core.api.FusionKey;
import com.ryderbelserion.fusion.core.api.enums.Level;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import java.nio.file.Path;

public class FusionVelocity extends FusionKyori<Audience, FileManager> {

    private final ComponentLogger logger;
    private final String namespace;

    public FusionVelocity(@NonNull final String namespace, @NonNull final ComponentLogger logger, @NonNull final Path path) {
        super(new FileManager(path), path);

        this.logger = logger;
        this.namespace = namespace;
    }

    @Override
    public void log(@NonNull final Level level, @NonNull final String message, @NonNull final Exception exception, @NonNull final Object... objects) {
        if (!this.isVerbose()) return;

        final Component component = asComponent(message.formatted(objects));

        switch (level) {
            case WARNING -> this.logger.warn(component, exception);
            case ERROR -> this.logger.error(component, exception);
            case INFO -> this.logger.info(component, exception);
        }
    }

    @Override
    public void log(@NonNull final Level level, @NonNull final String message, @NonNull final Object... objects) {
        if (!this.isVerbose()) return;

        final Component component = asComponent(message.formatted(objects));

        switch (level) {
            case WARNING -> this.logger.warn(component, objects);
            case ERROR -> this.logger.error(component, objects);
            case INFO -> this.logger.info(component, objects);
        }
    }

    @Override
    public final boolean isModReady(@NonNull final FusionKey key) {
        return false;
    }

    @Override
    public @NonNull final String getNamespace() {
        return this.namespace.toLowerCase();
    }

    @Override
    public @NonNull final String papi(@Nullable final Audience sender, @NonNull final String message) {
        return message;
    }
}