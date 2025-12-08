package com.ryderbelserion.fusion.addons.entrypoint.classloaders;

import com.ryderbelserion.fusion.addons.api.interfaces.IExtension;
import com.ryderbelserion.fusion.addons.api.interfaces.IExtensionMeta;
import com.ryderbelserion.fusion.addons.exceptions.InvalidExtensionException;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.lang.reflect.Constructor;
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

    protected final IExtension extension;
    protected final JarFile jarFile;
    protected final Path source;
    protected final Path path;
    protected final URL url;

    public SimpleExtensionClassLoader(@NotNull final Path path, @NotNull final Path source,
                                      @NotNull final IExtensionMeta extension, @NotNull final ClassLoader loader) throws IOException, InvalidExtensionException {
        super(path.getFileName().toString(), new URL[]{path.toUri().toURL()}, loader);

        this.jarFile = new JarFile(path.toFile());
        this.source = source;
        this.path = path;

        this.url = this.path.toUri().toURL();

        Class<?> jarClass;

        try {
            jarClass = Class.forName(extension.getMainClass(), true, this);

            this.classes.put(jarClass.getName(), jarClass);
        } catch (final ClassNotFoundException exception) {
            throw new InvalidExtensionException("Could not find main class %s,".formatted(extension.getMainClass()), exception);
        }

        Class<? extends IExtension> extensionClass;

        try {
            extensionClass = jarClass.asSubclass(IExtension.class);
        } catch (final Exception exception) {
            throw new InvalidExtensionException("Main Class %s must extend Extension!".formatted(extension.getMainClass()), exception);
        }

        Constructor<? extends IExtension> constructor;

        try {
            constructor = extensionClass.getDeclaredConstructor();
        } catch (final NoSuchMethodException exception) {
            throw new InvalidExtensionException("Main Class %s must have a no-args constructor".formatted(extension.getMainClass()), exception);
        }

        try {
            this.extension = constructor.newInstance();
        } catch (InstantiationException e) {
            throw new InvalidExtensionException("Main Class %s must not be abstract!".formatted(extension.getMainClass()));
        } catch (IllegalAccessException e) {
            throw new InvalidExtensionException("Main Class %s must be accessible!".formatted(extension.getMainClass()));
        } catch (InvocationTargetException e) {
            throw new InvalidExtensionException("Exception initializing main class %s!".formatted(extension.getMainClass()));
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