package com.ryderbelserion.fusion.addons.api.interfaces;

import org.tinylog.TaggedLogger;
import java.nio.file.Path;

public interface IExtensionMeta {

    Path getParentDirectory();

    Path getDataDirectory();

    String getMainClass();

    String getVersion();

    void info(final String message, final Object... params);

    void warn(final String message, final Object... params);

    void error(final String message, final Object... params);

    TaggedLogger getLogger();

    String getName();

}