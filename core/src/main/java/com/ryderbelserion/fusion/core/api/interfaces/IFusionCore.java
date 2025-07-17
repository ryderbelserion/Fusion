package com.ryderbelserion.fusion.core.api.interfaces;

import com.ryderbelserion.fusion.core.files.FileManager;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;
import java.util.List;

public interface IFusionCore {

    List<Path> getFiles(@NotNull final Path path, @NotNull final List<String> extensions, final int depth);

    List<Path> getFiles(@NotNull final Path path, @NotNull final String extension, final int depth);

    FileManager getFileManager();

    void setDataPath(@NotNull final Path dataPath);

    Path getDataPath();

}
