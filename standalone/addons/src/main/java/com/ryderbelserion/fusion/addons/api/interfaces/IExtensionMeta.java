package com.ryderbelserion.fusion.addons.api.interfaces;

import java.nio.file.Path;
import java.util.logging.Logger;

public interface IExtensionMeta {

    Path getParentDirectory();

    Path getDataDirectory();

    String getMainClass();

    String getVersion();

    void info(final String message, final Object... params);

    void warn(final String message, final Object... params);

    void error(final String message, final Object... params);

    Logger getLogger();

    String getName();

}