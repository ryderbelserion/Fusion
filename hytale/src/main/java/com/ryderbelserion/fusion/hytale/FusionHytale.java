package com.ryderbelserion.fusion.hytale;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.receiver.IMessageReceiver;
import com.ryderbelserion.fusion.api.enums.Level;
import com.ryderbelserion.fusion.core.FusionCore;
import fi.sulku.hytale.TinyMsg;
import org.jspecify.annotations.NullMarked;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@NullMarked
public abstract class FusionHytale extends FusionCore<IMessageReceiver, Message, String> {

    private final HytaleLogger logger;

    public FusionHytale(final HytaleLogger logger, final Path path) {
        super(path);

        this.logger = logger;
    }

    @Override
    public Message asComponent(final IMessageReceiver sender, final String message, final Map<String, String> placeholders, final List<String> tags) {
        return TinyMsg.parse(papi(sender, replacePlaceholders(message, placeholders)));
    }

    @Override
    public void log(final Level level, final String message, final Exception exception, final Object... args) {
        if (this.isVerbose()) return;

        final String format = message.formatted(args);

        switch (level) {
            case warn -> this.logger.atWarning().log(format, exception);
            case error -> this.logger.atSevere().log(format, exception);
            case info -> this.logger.atInfo().log(format, exception);
        }
    }

    @Override
    public void log(final Level level, final String message, final Object... args) {
        if (this.isVerbose()) return;

        final String format = message.formatted(args);

        switch (level) {
            case warn -> this.logger.atWarning().log(format);
            case error -> this.logger.atSevere().log(format);
            case info -> this.logger.atInfo().log(format);
        }
    }
}