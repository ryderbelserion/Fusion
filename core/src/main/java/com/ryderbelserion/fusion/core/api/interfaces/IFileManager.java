package com.ryderbelserion.fusion.core.api.interfaces;

import com.ryderbelserion.fusion.core.api.FusionCore;
import com.ryderbelserion.fusion.core.FusionProvider;
import com.ryderbelserion.fusion.core.api.enums.FileAction;
import com.ryderbelserion.fusion.core.api.enums.FileType;
import com.ryderbelserion.fusion.core.api.interfaces.files.ICustomFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationOptions;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * Loads, reloads, or saves existing files. It supports ConfigMe, Configurate-Jackson/Yaml,
 * and even supports loading/saving log files... It uses the {@link Path} as an identifier.
 */
public abstract class IFileManager {

    protected final FusionCore fusion = FusionProvider.get();
    protected final Path path = this.fusion.getPath();

    /**
     * Loads all folders manually added using FileManager#addFolder, usually used when you don't care to specify what you want.
     *
     * @param actions a list of actions to define what to do
     * @return {@link IFileManager}
     */
    public abstract @NotNull IFileManager init(@NotNull final List<FileAction> actions);

    /**
     * Adds a folder which is used to map Configurate yaml/jackson.
     *
     * @param folder the folder to extract/map
     * @param actions a list of actions to define what to do
     * @param options optional options to configure indentation size etc.
     * @return {@link IFileManager}
     */
    public abstract @NotNull IFileManager addFolder(@NotNull final Path folder, @NotNull final FileType fileType, @NotNull final List<FileAction> actions, @Nullable final UnaryOperator<ConfigurationOptions> options);

    /**
     * Adds a folder to a hashmap which is used with FileManager#init.
     *
     * @param folder the folder to add
     * @param fileType the type of file expected in the folder
     * @return {@link IFileManager}
     */
    public abstract @NotNull IFileManager addFolder(@NotNull final Path folder, @NotNull final FileType fileType);

    /**
     * Adds a folder which is used to map Configurate yaml/jackson or log files. It automatically determines the file type!
     *
     * @param path the path to extract/map
     * @param actions a list of actions to define what to do
     * @param options optional options to configure indentation size etc.
     * @return {@link IFileManager}
     */
    public abstract @NotNull IFileManager addFile(@NotNull final Path path, @NotNull final FileType fileType, @NotNull final List<FileAction> actions, @Nullable final UnaryOperator<ConfigurationOptions> options);

    /**
     * Saves contents at the given path i.e. a log file.
     *
     * @param path the path
     * @param actions a list of actions to define what to do
     * @param content the content to save to the log file
     * @return {@link IFileManager}
     */
    public abstract @NotNull IFileManager saveFile(@NotNull final Path path, @NotNull final List<FileAction> actions, @NotNull final String content);

    /**
     * Saves contents at a given path.
     *
     * @param path the path
     * @return {@link IFileManager}
     */
    public abstract @NotNull IFileManager saveFile(@NotNull final Path path);

    /**
     * Removes a file using {@link ICustomFile}.
     *
     * @param customFile the custom file
     * @param action the action to specify
     * @return {@link IFileManager}
     */
    public abstract @NotNull IFileManager removeFile(@NotNull final ICustomFile<? extends ICustomFile<?>> customFile, @Nullable final FileAction action);

    /**
     * Removes a file from the cache with the option to delete if the {@link FileAction} is specified.
     *
     * @param path the path
     * @param action the action
     * @return {@link IFileManager}
     */
    public abstract @NotNull IFileManager removeFile(@NotNull final Path path, @Nullable final FileAction action);

    /**
     * Purges all file data without saving or reloading.
     *
     * @return {@link IFileManager}
     */
    public abstract @NotNull IFileManager purge();

    /**
     * Extracts a folder with a specified list of actions.
     *
     * @param folder the folder to extract
     * @param actions the list of actions to define what to do.
     * @return {@link IFileManager}
     */
    public abstract @NotNull IFileManager extractFolder(@NotNull final Path folder, @NotNull final List<FileAction> actions);

    /**
     * Extracts from a path to the output folder.
     *
     * @param path the input path
     * @param output the output path
     * @param action the action type
     * @return {@link IFileManager}
     */
    public abstract @NotNull IFileManager extractResource(@NotNull final String path, @NotNull final String output, @NotNull final FileAction action);

    /**
     * Extracts a single resource to a specific path.
     *
     * @param path the input/output
     * @return {@link IFileManager}
     */
    public abstract @NotNull IFileManager extractResource(@NotNull final String path);

    /**
     * Saves or reloads all files.
     *
     * @param save true or false
     * @return {@link IFileManager}
     */
    public abstract @NotNull IFileManager refresh(final boolean save);

    /**
     * Fetches a generic custom file.
     *
     * @param path the path in the cache
     * @return {@link ICustomFile}
     */
    public abstract @Nullable ICustomFile<? extends ICustomFile<?>> getCustomFile(@NotNull final Path path);

    /**
     * Fetches all existing custom files.
     *
     * @return an unmodifiable map of custom files
     */
    public abstract @NotNull Map<Path, ICustomFile<? extends ICustomFile<?>>> getCustomFiles();
}