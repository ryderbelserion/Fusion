package com.ryderbelserion.fusion.hytale;

import com.hypixel.hytale.common.plugin.PluginIdentifier;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.PluginBase;
import com.hypixel.hytale.server.core.plugin.PluginManager;
import com.hypixel.hytale.server.core.receiver.IMessageReceiver;
import com.ryderbelserion.fusion.api.enums.Level;
import com.ryderbelserion.fusion.api.objects.FusionKey;
import com.ryderbelserion.fusion.hytale.interfaces.IFusionHytale;
import com.ryderbelserion.fusion.hytale.utils.ColorUtils;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import org.jspecify.annotations.NullMarked;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@NullMarked
public class FusionHytale extends FusionKyori<IMessageReceiver> implements IFusionHytale {

    private final HytaleLogger logger;
    private final JavaPlugin plugin;

    public FusionHytale(final JavaPlugin plugin, final Path path) {
        super(path);

        this.logger = plugin.getLogger();
        this.plugin = plugin;
    }

    @Override
    public boolean isModReady(final String key) {
        final PluginBase plugin = PluginManager.get().getPlugin(PluginIdentifier.fromString(key));

        return plugin != null && plugin.isEnabled();
    }

    @Override
    public String getNamespace() {
        return this.plugin.getName();
    }

    @Override
    public boolean isModReady(final FusionKey key) {
        return isModReady(key.getValue());
    }

    @Override
    public Message asMessage(final IMessageReceiver receiver, final String message, final Map<String, String> placeholders) {
        return ColorUtils.toHytale(asComponent(receiver, message, placeholders));
    }

    @Override
    public Message asMessage(final IMessageReceiver receiver, final String message) {
        return asMessage(receiver, message, new HashMap<>());
    }

    @Override
    public String papi(final IMessageReceiver sender, final String message) {
        return message;
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