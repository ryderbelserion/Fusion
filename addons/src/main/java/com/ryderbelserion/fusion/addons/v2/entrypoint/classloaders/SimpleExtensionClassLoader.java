package com.ryderbelserion.fusion.addons.v2.entrypoint.classloaders;

import com.ryderbelserion.fusion.addons.v2.api.interfaces.IExtension;
import com.ryderbelserion.fusion.addons.v2.api.interfaces.IExtensionMeta;
import com.ryderbelserion.fusion.addons.v2.exceptions.InvalidExtensionException;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarFile;

public class SimpleExtensionClassLoader extends URLClassLoader {

    private final Map<String, Class<?>> classes = new ConcurrentHashMap<>();

    protected final IExtensionMeta config;
    protected final IExtension extension;
    protected final JarFile jarFile;
    protected final Path source;
    protected final Path path;
    protected final URL url;

    public SimpleExtensionClassLoader(@NotNull final Path path, @NotNull final Path source,
                                      @NotNull final IExtensionMeta config, @NotNull final ClassLoader loader) throws IOException, InvalidExtensionException {
        super(path.getFileName().toString(), new URL[]{path.toUri().toURL()}, loader);

        this.jarFile = new JarFile(path.toFile());
        this.source = source;
        this.config = config;
        this.path = path;

        this.url = this.path.toUri().toURL();

        Class<?> mainClass;

        try {
            mainClass = Class.forName(this.config.getMainClass(), true, this);

            this.classes.put(mainClass.getName(), mainClass);
        } catch (final ClassNotFoundException exception) {
            throw new InvalidExtensionException("Could not find main class %s,".formatted(this.config.getMainClass()), exception);
        }

        Class<? extends IExtension> extension;

        try {
            extension = mainClass.asSubclass(IExtension.class);
        } catch (final Exception exception) {
            throw new InvalidExtensionException("Main Class %s must extend Extension!".formatted(this.config.getMainClass()), exception);
        }

        try {
            this.extension = extension.getDeclaredConstructor().newInstance();
        } catch (final IllegalAccessException | NoSuchMethodException | InstantiationException |
                       InvocationTargetException exception) {
            throw new InvalidExtensionException("Failed to load main class for the extension %s!".formatted(this.config.getName()), exception);
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> result = this.classes.get(name);

        if (result == null) {
            this.classes.put(name, result = super.findClass(name));
        }

        return result;
    }

    public @NotNull Collection<Class<?>> getClasses() {
        return this.classes.values();
    }
}