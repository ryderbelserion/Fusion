package com.ryderbelserion.fusion.api;

import com.ryderbelserion.fusion.api.enums.Level;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class FusionApi {

    public abstract boolean isVerbose();

    public abstract void log(
            final Level level,
            final String message,
            final Exception exception,
            final Object... args
    );

    public abstract void log(
            final Level level,
            final String message,
            final Object... args
    );
}