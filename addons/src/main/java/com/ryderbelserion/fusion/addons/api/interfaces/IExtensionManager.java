package com.ryderbelserion.fusion.addons.api.interfaces;

import com.ryderbelserion.fusion.addons.api.Extension;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;
import java.util.Optional;

public interface IExtensionManager {

    void init(final int depth);

    void loadExtension(@NotNull final Path path);

    void disableExtension(@NotNull final Extension extension);

    boolean isExtensionEnabled(@NotNull final String name);

    Optional<Extension> getExtension(@NotNull final String name);

    void purge();

}