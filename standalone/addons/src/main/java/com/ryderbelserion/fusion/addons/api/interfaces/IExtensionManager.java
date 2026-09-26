package com.ryderbelserion.fusion.addons.api.interfaces;

import com.ryderbelserion.fusion.addons.api.Extension;
import org.jspecify.annotations.NonNull;
import java.nio.file.Path;
import java.util.Optional;

public interface IExtensionManager {

    void init(final int depth);

    void loadExtension(@NonNull final Path path);

    void disableExtension(@NonNull final Extension extension);

    boolean isExtensionEnabled(@NonNull final String name);

    Optional<Extension> getExtension(@NonNull final String name);

    void purge();

}