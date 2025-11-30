package com.ryderbelserion.fusion.addons.v2.api.interfaces;

import org.slf4j.Logger;
import java.nio.file.Path;

public interface IExtensionMeta {

    Path getDataDirectory();

    String getMainClass();

    String getVersion();

    Logger getLogger();

    String getName();

}