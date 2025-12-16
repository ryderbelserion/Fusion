package com.ryderbelserion.fusion.core.api.interfaces;

import com.ryderbelserion.fusion.files.FileManager;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.Path;

public interface IFusionCore {

    void deleteDirectory(@NotNull final Path path) throws IOException;

    FileManager getFileManager();

    String getNumberFormat();

    String getItemsPlugin();

    String getRounding();

    boolean isVerbose();

    Path getDataPath();

    int getDepth();

}