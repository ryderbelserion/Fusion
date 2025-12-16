package com.ryderbelserion.fusion.files.interfaces;

import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.fusion.files.enums.FileAction;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.files.types.JaluCustomFile;
import com.ryderbelserion.fusion.files.types.LogCustomFile;
import com.ryderbelserion.fusion.files.types.configurate.JsonCustomFile;
import com.ryderbelserion.fusion.files.types.configurate.YamlCustomFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class IFileManager<I> {

    public abstract @NotNull I addFolder(@NotNull final Path folder, @NotNull final Consumer<YamlFileResourceOptions.Builder> options, @NotNull final Consumer<SettingsManagerBuilder> builder);

    public abstract @NotNull I addFile(@NotNull final Path path, @NotNull final Consumer<YamlFileResourceOptions.Builder> options, @NotNull final Consumer<SettingsManagerBuilder> builder);

    public abstract @NotNull I addFolder(@NotNull final Path folder, @NotNull final FileType fileType, @NotNull final Consumer<ICustomFile<?, ?, ?, ?>> consumer);

    public @NotNull I addFolder(@NotNull final Path folder, @NotNull final FileType fileType) {
        return addFolder(folder, fileType, consumer -> consumer.addAction(FileAction.EXTRACT_FOLDER));
    }

    public abstract @NotNull I addFile(@NotNull final Path path, @NotNull final FileType fileType, @NotNull final Consumer<ICustomFile<?, ?, ?, ?>> consumer);

    public @NotNull I addFile(@NotNull final Path path, @NotNull final FileType fileType) {
        return addFile(path, fileType, consumer -> consumer.addAction(FileAction.EXTRACT_FILE));
    }

    public abstract @NotNull I addFile(@NotNull final Path path, @NotNull final ICustomFile<?, ?, ?, ?> customFile);

    public abstract @NotNull I removeFile(@NotNull final Path path);

    public abstract @NotNull I reloadFile(@NotNull final Path path);

    public abstract @NotNull I saveFile(@NotNull final Path path);

    public abstract @NotNull I purge();

    public abstract @NotNull I refresh(final boolean save);

    public abstract boolean hasFile(@NotNull final Path path);

    public abstract @NotNull Optional<ICustomFile<?, ?, ?, ?>> getFile(@NotNull final Path path);

    public abstract @NotNull Map<Path, ICustomFile<?, ?, ?, ?>> getFiles();

    public abstract @NotNull JaluCustomFile buildJaluFile(@NotNull final Path path, @NotNull final Consumer<YamlFileResourceOptions.Builder> options, @NotNull final Consumer<SettingsManagerBuilder> builder);

    public abstract @NotNull YamlCustomFile buildYamlFile(@NotNull final Path path, @NotNull final Consumer<YamlCustomFile> consumer);

    public abstract @NotNull JsonCustomFile buildJsonFile(@NotNull final Path path, @NotNull final Consumer<JsonCustomFile> consumer);

    public abstract @NotNull LogCustomFile buildLogFile(@NotNull final Path path, @NotNull final Consumer<LogCustomFile> consumer);

    public @NotNull Optional<YamlCustomFile> getYamlFile(@NotNull final Path path) {
        return getFile(path).map(YamlCustomFile.class::cast);
    }

    public @NotNull Optional<JsonCustomFile> getJsonFile(@NotNull final Path path) {
        return getFile(path).map(JsonCustomFile.class::cast);
    }

    public @NotNull Optional<JaluCustomFile> getJaluFile(@NotNull final Path path) {
        return getFile(path).map(JaluCustomFile.class::cast);
    }

    public @NotNull Optional<LogCustomFile> getLogFile(@NotNull final Path path) {
        return getFile(path).map(LogCustomFile.class::cast);
    }

    public abstract @NotNull I extractFolder(@NotNull final Path jarPath, @NotNull final String folder, @NotNull final Path output);

    public abstract @NotNull I extractFile(@NotNull final Path jarPath, @NotNull final String fileName, @NotNull final Path output);

    public abstract @NotNull I extractFile(@NotNull final Path jarPath, @NotNull final Path path);

    public abstract @NotNull I compressFolder(@NotNull final Path path, @NotNull final String content);

    public abstract @NotNull I compressFile(@NotNull final Path path, @Nullable final Path folder, @NotNull final String content);

    public abstract @NotNull I writeFile(@NotNull final Path path, @NotNull final String content);

    public abstract @NotNull List<String> getFileNames(@NotNull final String folder, @NotNull final Path path, @NotNull final String extension, final int depth, final boolean removeExtension);

    public @NotNull List<String> getFileNames(@NotNull final String folder, @NotNull final Path path, @NotNull final String extension, final boolean removeExtension) {
        return getFileNames(folder, path, extension, 1, removeExtension);
    }

    public abstract @NotNull List<Path> getFiles(@NotNull final Path path, @NotNull final List<String> extensions, final int depth);

    public @NotNull List<Path> getFiles(@NotNull final Path path, @NotNull final String extension, final int depth) {
        return getFiles(path, List.of(extension), depth);
    }

    public abstract int getDirectorySize(@NotNull final Path path, @NotNull final String extension);

    public @NotNull I compressFile(@NotNull final Path path, @NotNull final String content) {
        return compressFile(path, null, content);
    }

    public @NotNull I compressFile(@NotNull final Path path, @NotNull final Path folder) {
        return compressFile(path, folder, "");
    }

    public @NotNull I compressFile(@NotNull final Path path) {
        return compressFile(path, null, "");
    }

    public abstract void setDepth(final int depth);

    public abstract int getDepth();

    protected String asString(@NotNull final Path path, @NotNull final String content) {
        final StringBuilder builder = new StringBuilder();

        builder.append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        if (!content.isEmpty()) builder.append(content);

        return builder.append("-").append(getDirectorySize(path, ".gz")).append(".gz").toString();
    }
}