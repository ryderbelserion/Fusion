package com.ryderbelserion.fusion.addons.entrypoint.classloaders;

import com.ryderbelserion.fusion.addons.api.Extension;
import com.ryderbelserion.fusion.addons.api.meta.ExtensionMeta;
import com.ryderbelserion.fusion.addons.exceptions.InvalidExtensionException;
import org.jspecify.annotations.NonNull;
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

    protected final Extension extension;
    protected final JarFile jarFile;
    protected final Path parent;
    protected final Path path;
    protected final URL url;

    protected boolean isDisabling;

    public SimpleExtensionClassLoader(@NonNull final Path path, @NonNull final Path parent,
                                      @NonNull final ExtensionMeta extensionMeta, @NonNull final ClassLoader loader) throws IOException, InvalidExtensionException {
        super(path.getFileName().toString(), new URL[]{path.toUri().toURL()}, loader);

        extensionMeta.init(this.parent = parent, this.path = path);

        final String mainClass = extensionMeta.getMainClass();

        this.jarFile = new JarFile(this.path.toFile());
        this.url = this.path.toUri().toURL();

        Class<?> jarClass;

        try {
            jarClass = Class.forName(mainClass, true, this);

            this.classes.put(jarClass.getName(), jarClass);
        } catch (final ClassNotFoundException exception) {
            throw new InvalidExtensionException("Could not find main class %s,".formatted(mainClass), exception);
        }

        Class<? extends Extension> extensionClass;

        try {
            extensionClass = jarClass.asSubclass(Extension.class);
        } catch (final Exception exception) {
            throw new InvalidExtensionException("Main Class %s must extend Extension!".formatted(mainClass), exception);
        }

        Constructor<? extends Extension> constructor;

        try {
            constructor = extensionClass.getDeclaredConstructor();
        } catch (final NoSuchMethodException exception) {
            throw new InvalidExtensionException("Main Class %s must have a no-args constructor".formatted(mainClass), exception);
        }

        try {
            this.extension = constructor.newInstance();
            this.extension.copy(extensionMeta);
        } catch (final InstantiationException exception) {
            throw new InvalidExtensionException("Main Class %s must not be abstract!".formatted(mainClass), exception);
        } catch (final IllegalAccessException exception) {
            throw new InvalidExtensionException("Main Class %s must be accessible!".formatted(mainClass), exception);
        } catch (final InvocationTargetException exception) {
            throw new InvalidExtensionException("Exception initializing main class %s!".formatted(mainClass), exception);
        }
    }

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        if (this.isDisabling()) {
            throw new ClassNotFoundException("This class loader is disabled!");
        }

        Class<?> result = this.classes.get(name);

        if (result == null) {
            this.classes.put(name, result = super.findClass(name));
        }

        return result;
    }

    public void removeClasses() {
        if (!this.isDisabling()) {
            throw new IllegalStateException("Cannot remove class when the loader isn't disabled!");
        }

        this.classes.clear();
    }

    public void setDisabling(final boolean isDisabling) {
        this.isDisabling = isDisabling;
    }

    public boolean isDisabling() {
        return this.isDisabling;
    }

    public @NonNull Collection<Class<?>> getClasses() {
        return this.classes.values();
    }

    public @NonNull Extension getExtension() {
        return this.extension;
    }
}