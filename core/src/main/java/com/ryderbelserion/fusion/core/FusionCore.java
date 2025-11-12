package com.ryderbelserion.fusion.core;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import com.ryderbelserion.fusion.core.config.FusionConfig;
import com.ryderbelserion.fusion.core.interfaces.IFusionCore;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public abstract class FusionCore implements IFusionCore {

    protected final SettingsManager config;
    private final Path path;

    public FusionCore(@NotNull final Path path) {
        this.config = SettingsManagerBuilder.withYamlFile(path.resolve("fusion.yml")).configurationData(FusionConfig.class).useDefaultMigrationService().create();
        this.path = path;
    }

    public String replacePlaceholder(@NotNull final String message, @NotNull final Map<String, String> placeholders) {
        String safeMessage = message;

        if (!placeholders.isEmpty()) {
            for (final Map.Entry<String, String> key : placeholders.entrySet()) {
                if (key == null) continue;

                final String placeholder = key.getKey();
                final String value = key.getValue();

                if (placeholder != null && value != null) {
                    safeMessage = safeMessage.replace(placeholder, value).replace(placeholder.toLowerCase(), value);
                }
            }
        }

        return safeMessage;
    }

    public abstract boolean isModReady(@NotNull final FusionKey key);

    public abstract FusionCore reload();

    @Override
    public void deleteDirectory(@NotNull final Path path) throws IOException {
        if (!Files.exists(path) || !Files.isDirectory(path)) return;

        try (final DirectoryStream<Path> contents = Files.newDirectoryStream(path)) {
            for (final Path entry : contents) {
                if (Files.isDirectory(entry)) {
                    deleteDirectory(entry);

                    continue;
                }

                Files.delete(entry);
            }
        }

        Files.deleteIfExists(path);
    }

    @Override
    public @NotNull final String getItemsPlugin() {
        return this.config.getProperty(FusionConfig.custom_items_plugin);
    }

    @Override
    public @NotNull final String getNumberFormat() {
        return this.config.getProperty(FusionConfig.number_format);
    }

    @Override
    public @NotNull final String getRounding() {
        return this.config.getProperty(FusionConfig.rounding_format);
    }

    @Override
    public @NotNull final Path getDataPath() {
        return this.path;
    }

    @Override
    public final boolean isVerbose() {
        return this.config.getProperty(FusionConfig.is_verbose);
    }

    @Override
    public final int getDepth() {
        return this.config.getProperty(FusionConfig.recursion_depth);
    }
}