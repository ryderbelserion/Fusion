package com.ryderbelserion.fusion.core.api.interfaces;

import com.ryderbelserion.fusion.core.FusionConfig;
import com.ryderbelserion.fusion.core.api.support.ModManager;
import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.core.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface IFusionCore {

    List<String> getFileNames(@NotNull final String folder, @NotNull final Path path, @NotNull final String extension, final int depth, final boolean withoutExtension);

    default List<String> getFileNames(@NotNull final String folder, @NotNull final Path path, @NotNull final String extension, final boolean withoutExtension) {
        return getFileNames(folder, path, extension, this.getConfig().getDepth(), withoutExtension);
    }

    void deleteDirectory(@NotNull final Path path) throws IOException;

    List<Path> getFiles(@NotNull final Path path, @NotNull final List<String> extensions, final int depth);

    List<Path> getFiles(@NotNull final Path path, @NotNull final String extension, final int depth);

    StringUtils getStringUtils();

    FileManager getFileManager();

    ModManager getModManager();

    FusionConfig getConfig();

    void setDataPath(@NotNull final Path dataPath);

    Path getDataPath();

}