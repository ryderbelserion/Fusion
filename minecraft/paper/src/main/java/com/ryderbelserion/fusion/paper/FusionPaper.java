package com.ryderbelserion.fusion.paper;

import com.ryderbelserion.fusion.core.utils.StringUtils;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FusionPaper extends FusionKyori {

    private final FileManager fileManager;

    public FusionPaper(@NotNull final Path path) {
        this.fileManager = new FileManager(path);
    }

    @Override
    public @NotNull final Component parse(@NotNull final Audience audience, @NotNull final String message, @NotNull final Map<String, String> placeholders, @NotNull final List<TagResolver> tags) {
        final List<TagResolver> resolvers = new ArrayList<>(tags);

        placeholders.forEach((key, value) -> resolvers.add(Placeholder.parsed(StringUtils.replaceAllBrackets(key).toLowerCase(), value)));

        return MiniMessage.miniMessage().deserialize(papi(audience, StringUtils.replaceBrackets(message)), TagResolver.resolver(resolvers)).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    @Override
    public @NotNull final String papi(@NotNull final Audience audience, @NotNull final String message) {
        return "";
    }

    public @NotNull final FileManager getFileManager() {
        return this.fileManager;
    }
}