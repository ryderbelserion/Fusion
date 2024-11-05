package com.ryderbelserion.vital.api.files;

import com.ryderbelserion.vital.VitalProvider;
import com.ryderbelserion.vital.api.Vital;
import com.ryderbelserion.vital.api.exceptions.GenericException;
import com.ryderbelserion.vital.api.exceptions.UnavailableException;
import com.ryderbelserion.vital.api.files.enums.FileType;
import com.ryderbelserion.vital.api.files.types.YamlCustomFile;
import com.ryderbelserion.vital.utils.Methods;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages custom files for the application.
 *
 * <p>This class provides functionality to add, remove, and retrieve custom file instances,
 * supporting various file types such as YAML. It ensures that file operations are handled
 * efficiently and provides logging for important actions.
 *
 * @version 0.0.5
 * @since 0.0.5
 * @author ryderbelserion
 */
public class FileManager {

    private final Vital api = VitalProvider.get();
    private final ComponentLogger logger = this.api.getLogger();
    private final File dataFolder = this.api.getDataFolder();
    private final boolean isVerbose = this.api.isVerbose();

    private final Map<String, CustomFile<? extends CustomFile<?>>> files = new HashMap<>();

    /**
     * Default constructor.
     *
     * @since 0.0.5
     */
    public FileManager() {}

    /**
     * Adds a folder to the manager and loads its contents.
     *
     * @param folder the folder name
     * @param fileType the type of files in the folder
     * @return the current instance of {@link FileManager}
     * @since 0.0.5
     */
    public final FileManager addFolder(final String folder, final FileType fileType) {
        final File directory = new File(this.dataFolder, folder);

        if (!directory.exists()) {
            directory.mkdirs();

            Methods.extracts(FileManager.class, String.format("/%s", directory.getName()), directory.toPath(), false);
        }

        final File[] contents = directory.listFiles();

        if (contents == null) return this;

        final String extension = fileType.getExtension();

        for (final File file : contents) {
            if (file.isDirectory()) {
                final String[] files = file.list();

                if (files == null) continue;

                for (final String fileName : files) {
                    if (!fileName.endsWith("." + extension)) continue; // just in case people are weird

                    addFile(fileName, fileType);
                }

                continue;
            }

            final String fileName = file.getName();

            if (!fileName.endsWith("." + extension)) continue; // just in case people are weird

            addFile(fileName, fileType);
        }

        return this;
    }

    /**
     * Adds a custom file with a default file type.
     *
     * <p>This method adds a file to the manager's map using the default file type of {@link FileType#NONE}.
     *
     * @param fileName the name of the file to add
     * @return the current instance of {@link FileManager}
     * @since 0.0.5
     */
    public final FileManager addFile(final String fileName) {
        return addFile(fileName, FileType.NONE);
    }

    /**
     * Adds a custom file to the manager's map.
     *
     * <p>This method supports adding YAML files and will throw an exception for unsupported file types.
     *
     * @param fileName the name of the file to add
     * @param fileType the type of the file
     * @return the current instance of {@link FileManager}
     * @since 0.0.5
     */
    public final FileManager addFile(final String fileName, final FileType fileType) {
        if (fileName == null || fileName.isEmpty()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot add the file as the file is null or empty.");
            }

            return this;
        }

        final String extension = fileType.getExtension();

        final String strippedName = strip(fileName, extension);

        final File file = new File(this.dataFolder, fileName);

        this.api.saveResource(file.getPath(), false);

        switch (fileType) {
            case YAML -> this.files.put(strippedName, new YamlCustomFile(file).loadConfiguration());

            case JSON -> throw new GenericException("The file type with extension " + extension + " is not currently supported.");

            case NONE -> {} // do nothing
        }

        return this;
    }

    /**
     * Removes a custom file from the manager's map by its instance.
     *
     * <p>This method removes the file if it exists in the map.
     *
     * @param customFile the custom file instance to remove
     * @param purge whether to delete the physical file
     * @return the current instance of {@link FileManager}
     * @since 0.0.5
     */
    public final FileManager removeFile(final CustomFile<? extends CustomFile<?>> customFile, final boolean purge) {
        return removeFile(customFile.getFileName(), customFile.getFileType(), purge);
    }

    /**
     * Removes a custom file from the manager's map by its name.
     *
     * <p>This method removes the file if it exists in the map.
     *
     * @param fileName the name of the file to remove
     * @param fileType the type of the file
     * @param purge whether to delete the physical file
     * @return the current instance of {@link FileManager}
     * @since 0.0.5
     */
    public final FileManager removeFile(final String fileName, final FileType fileType, final boolean purge) {
        final String strippedName = strip(fileName, fileType.getExtension());

        if (!this.files.containsKey(strippedName)) return this;

        final CustomFile<? extends CustomFile<?>> customFile = this.files.remove(fileName);

        if (purge) {
            final File file = customFile.getFile();

            if (file == null) return this;

            if (file.delete()) {
                if (this.isVerbose) {
                    this.logger.warn("Successfully deleted {}", fileName);
                }
            }

            return this;
        }

        customFile.saveConfiguration();

        return this;
    }

    /**
     * Retrieves a custom file from the manager's map by its name.
     *
     * <p>This method returns the custom file instance if it exists in the map.
     *
     * @param fileName the name of the file to retrieve
     * @return the custom file instance, or null if not found
     * @since 0.0.5
     */
    public CustomFile<? extends CustomFile<?>> getFile(final String fileName) {
        return this.files.get(fileName);
    }

    /**
     * Removes the extension from the file name.
     *
     * @param fileName the file name to strip the extension from
     * @param extension the extension to remove
     * @return the file name without the extension
     * @since 0.0.5
     */
    public final String strip(final String fileName, final String extension) {
        return fileName.replace("." + extension, "");
    }

    /**
     * Retrieves the map of custom files managed by this file manager.
     *
     * @return a map of custom files with their file names as keys
     * @since 0.0.5
     */
    public Map<String, CustomFile<? extends CustomFile<?>>> getFiles() {
        return this.files;
    }
}