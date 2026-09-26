package com.ryderbelserion.fusion.addons.api.interfaces;

import org.tinylog.TaggedLogger;
import java.nio.file.Path;

public interface IExtensionMeta {

    Path getDataDirectory();

    String getMainClass();

    String getVersion();

    void info(final String message);

    void warn(final String message);

    void error(final String message);

    TaggedLogger getLogger();

    String getName();

}