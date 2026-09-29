package com.ryderbelserion.fusion.addons.api.interfaces;

import com.ryderbelserion.fusion.addons.api.Extension;
import com.ryderbelserion.fusion.addons.exceptions.InvalidExtensionException;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public interface IExtensionManager {

    void init(final int depth);

    void loadExtension(@NonNull final Path path) throws IOException, InvalidExtensionException;

    void disableExtension(@NonNull final Extension extension);

    void reloadExtension(@NonNull final Extension extension);

    boolean isExtensionEnabled(@NonNull final String name);

    Optional<Extension> getExtension(@NonNull final String name);

    void purge();

    void info(final String message, final Object... params);

    void warn(final String message, final Object... params);

    void error(final String message, final Object... params);

}