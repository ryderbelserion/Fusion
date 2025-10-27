package com.ryderbelserion.fusion.core.interfaces;

import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.Path;

public interface IFusionCore {

    void deleteDirectory(@NotNull final Path path) throws IOException;

    String getNumberFormat();

    String getItemsPlugin();

    String getRounding();

    boolean isVerbose();

    Path getDataPath();

    int getDepth();

}