package com.ryderbelserion.fusion.api;

import com.ryderbelserion.fusion.api.exceptions.FusionException;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullUnmarked;
import org.tinylog.Level;
import org.tinylog.Logger;

@NullMarked
public abstract class FusionApi {

    public abstract boolean isVerbose();

    public void log(final Level level, final Exception exception, final String message, final Object... args) {
        final boolean isVerbose = isVerbose();

        switch (level) {
            case ERROR -> Logger.error(exception, message, args);
            case DEBUG -> {
                if (isVerbose) {
                    Logger.debug(exception, message, args);
                }
            }
            case WARN -> {
                if (isVerbose) {
                    Logger.warn(exception, message, args);
                }
            }
            case INFO -> {
                if (isVerbose) {
                    Logger.info(exception, message, args);
                }
            }
        }
    }

    public void log(final Level level, final String message, final Object... args) {
        final boolean isVerbose = isVerbose();

        switch (level) {
            case ERROR -> Logger.error(message, args);
            case DEBUG -> {
                if (isVerbose) {
                    Logger.debug(message, args);
                }
            }
            case WARN -> {
                if (isVerbose) {
                    Logger.warn(message, args);
                }
            }
            case INFO -> {
                if (isVerbose) {
                    Logger.info(message, args);
                }
            }
        }
    }

    @NullUnmarked
    class Provider {

        private static FusionApi fusion;

        public static FusionApi api() {
            if (Provider.fusion == null) {
                throw new FusionException("Failed to fetch Fusion API, is it enabled?");
            }

            return Provider.fusion;
        }

        public static void register(final FusionApi fusion) {
            Provider.fusion = fusion;
        }

        public static void unregister() {
            Provider.fusion = null;
        }

        public static boolean isReady() {
            return Provider.fusion != null;
        }
    }
}