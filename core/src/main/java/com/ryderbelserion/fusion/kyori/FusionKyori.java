package com.ryderbelserion.fusion.kyori;

import ch.jalu.configme.SettingsManagerBuilder;
import com.ryderbelserion.fusion.kyori.components.KyoriLogger;
import com.ryderbelserion.fusion.kyori.utils.AdvUtils;
import com.ryderbelserion.fusion.kyori.utils.StringUtils;
import com.ryderbelserion.fusion.core.FusionCore;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class FusionKyori extends FusionCore {

    protected final KyoriLogger logger;

    protected FusionKyori(@NotNull ComponentLogger logger, @NotNull Path path, @NotNull Consumer<SettingsManagerBuilder> consumer) {
        super(path, consumer);

        this.logger = new KyoriLogger(logger, this);
    }

    public abstract String parsePlaceholders(@NotNull Audience audience, @NotNull String message);

    public abstract @NotNull String chomp(@NotNull String message);

    public abstract boolean isPluginEnabled(@NotNull String name);

    @Override
    public @NotNull final KyoriLogger getLogger() {
        return this.logger;
    }

    public @NotNull Component color(@NotNull Audience audience, @NotNull String message, @NotNull Map<String, String> placeholders, @NotNull List<TagResolver> tags) {
        List<TagResolver> resolvers = new ArrayList<>(tags);

        placeholders.forEach((placeholder, value) -> resolvers.add(Placeholder.parsed(StringUtils.replaceAllBrackets(placeholder).toLowerCase(), value)));

        return AdvUtils.parse(parsePlaceholders(audience, StringUtils.replaceBrackets(message)), TagResolver.resolver(resolvers));
    }

    public @NotNull Component color(@NotNull Audience audience, @NotNull String message, @NotNull Map<String, String> placeholders) {
        return color(audience, message, placeholders, List.of());
    }

    public @NotNull Component color(@NotNull String message, @NotNull Map<String, String> placeholders) {
        return color(Audience.empty(), message, placeholders);
    }

    public @NotNull Component color(@NotNull String message) {
        return color(Audience.empty(), message, new HashMap<>());
    }

    public void sendMessage(@NotNull Audience audience, @NotNull String message, @NotNull Map<String, String> placeholders) {
        audience.sendMessage(color(audience, message, placeholders));
    }

    public void sendMessage(@NotNull Audience audience, @NotNull List<String> messages, @NotNull Map<String, String> placeholders) {
        messages.forEach(message -> sendMessage(audience, message, placeholders));
    }
}