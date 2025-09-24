package com.ryderbelserion.fusion.core.api.interfaces;

import com.ryderbelserion.fusion.core.api.support.ModManager;
import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.core.utils.StringUtils;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface IFusionCore {

    List<String> getFileNames(@NotNull final String folder, @NotNull final Path path, @NotNull final String extension, final int depth, final boolean removeExtension);

    default List<String> getFileNames(@NotNull final String folder, @NotNull final Path path, @NotNull final String extension, final boolean removeExtension) {
        return getFileNames(folder, path, extension, getDepth(), removeExtension);
    }

    void deleteDirectory(@NotNull final Path path) throws IOException;

    List<Path> getFiles(@NotNull final Path path, @NotNull final List<String> extensions, final int depth);

    List<Path> getFiles(@NotNull final Path path, @NotNull final String extension, final int depth);

    default List<Path> getFiles(@NotNull final Path path, @NotNull final String extension) {
        return getFiles(path, extension, getDepth());
    }

    StringUtils getStringUtils();

    FileManager getFileManager();

    ModManager getModManager();

    void setDataPath(@NotNull final Path dataPath);

    void setLogger(@NotNull final ComponentLogger logger);

    Path getDataPath();

    String getRounding();

    String getNumberFormat();

    String getCustomItemsPlugin();

    boolean isVerbose();

    int getDepth();

}