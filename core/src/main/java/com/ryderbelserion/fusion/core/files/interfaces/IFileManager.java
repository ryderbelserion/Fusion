package com.ryderbelserion.fusion.core.files.interfaces;

import ch.jalu.configme.SettingsManagerBuilder;
import com.ryderbelserion.fusion.core.files.enums.FileType;
import com.ryderbelserion.fusion.core.files.types.JaluCustomFile;
import com.ryderbelserion.fusion.core.files.types.JsonCustomFile;
import com.ryderbelserion.fusion.core.files.types.LogCustomFile;
import com.ryderbelserion.fusion.core.files.types.YamlCustomFile;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.nio.file.Path;
import java.util.function.Consumer;

public abstract class IFileManager<I> {

    public abstract @NotNull I addFolder(@NotNull final Path folder, @NotNull final FileType fileType, @NotNull final Consumer<ICustomFile<?, ?, ?, ?>> consumer);

    public abstract @NotNull I addFolder(@NotNull final Path folder, @NotNull final Consumer<SettingsManagerBuilder> builder);

    public abstract @NotNull I addFolder(@NotNull final Path folder, @NotNull final FileType fileType);

    public abstract @NotNull I addFile(@NotNull final Key key, @NotNull final Consumer<SettingsManagerBuilder> builder);

    public abstract @NotNull I addFile(@NotNull final Key key, @NotNull final FileType fileType, @NotNull final Consumer<ICustomFile<?, ?, ?, ?>> consumer);

    public abstract @NotNull I removeFile(@NotNull final Key key);

    public abstract @NotNull I reloadFile(@NotNull final Key key);

    public abstract @NotNull I purge();

    public abstract @NotNull ICustomFile<?, ?, ?, ?> getFile(@NotNull final Key key);

    public abstract @NotNull JaluCustomFile buildJaluFile(@NotNull final Consumer<SettingsManagerBuilder> builder);

    public abstract @NotNull YamlCustomFile buildYamlFile(@NotNull final Consumer<YamlCustomFile> consumer);

    public abstract @NotNull JsonCustomFile buildJsonFile(@NotNull final Consumer<JsonCustomFile> consumer);

    public abstract @NotNull LogCustomFile buildLogFile(@NotNull final Consumer<LogCustomFile> consumer);

    public @NotNull YamlCustomFile getYamlFile(@NotNull final Key key) {
        return (YamlCustomFile) getFile(key);
    }

    public @NotNull JaluCustomFile getJaluFile(@NotNull final Key key) {
        return (JaluCustomFile) getFile(key);
    }

    public @NotNull LogCustomFile getLogFile(@NotNull final Key key) {
        return (LogCustomFile) getFile(key);
    }

    public abstract @NotNull I extractFolder(@NotNull final String folder, @NotNull final Path output);

    public abstract @NotNull I extractFile(@NotNull final Path path);

    public abstract @NotNull I compressFolder(@NotNull final Path path, @NotNull final String content);

    public abstract @NotNull I compressFile(@NotNull final Path path, @Nullable final Path folder, @NotNull final String content);

    public abstract @NotNull I writeFile(@NotNull final Path path, @NotNull final String content);

    public abstract int getFileCount(@NotNull final Path path, @NotNull final String extension);

    public @NotNull I compressFile(@NotNull final Path path, @NotNull final String content) {
        return compressFile(path, null, content);
    }

    public @NotNull I compressFile(@NotNull final Path path, @NotNull final Path folder) {
        return compressFile(path, folder, "");
    }

    public @NotNull I compressFile(@NotNull final Path path) {
        return compressFile(path, null, "");
    }
}