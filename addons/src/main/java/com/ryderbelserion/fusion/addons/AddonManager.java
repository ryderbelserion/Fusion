package com.ryderbelserion.fusion.addons;

import com.ryderbelserion.fusion.addons.interfaces.IAddon;
import com.ryderbelserion.fusion.addons.objects.Addon;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages addons, including loading it into the class path, and caching it all.
 */
public class AddonManager {

    private final Map<String, Class<?>> classMap = new ConcurrentHashMap<>();
    private final Map<String, AddonClassLoader> loaders = new ConcurrentHashMap<>();

    private final Path folder;
    private final Path path;

    /**
     * The default constructor to create an instance of {@link AddonManager}
     *
     * @param path the root path
     */
    public AddonManager(@NotNull final Path path) {
        this.folder = path.resolve("addons");

        this.path = path;
    }

    /**
     * Purges all classes found in the map passed through.
     *
     * @param classes the map of classes
     */
    public void removeAll(@NotNull final Map<String, Class<?>> classes) {
        classes.keySet().forEach(this.classMap::remove);

        for (final Class<?> object : classes.values()) {
            if (this.classMap.containsValue(object)) {
                throw new IllegalStateException(String.format("Class %s did not get removed.", object.getName()));
            }
        }
    }

    /**
     * Adds a class path to the concurrent cash.
     *
     * @param name        the name of the class
     * @param classObject the class object
     */
    public void setClass(@NotNull final String name, @NotNull final Class<?> classObject) {
        this.classMap.put(name, classObject);
    }

    /**
     * Retrieves a class object by name.
     *
     * @param name the name of the class
     * @return {@link Class}
     */
    public Class<?> findClass(@NotNull final String name) {
        Class<?> classObject = null;

        if (this.classMap.containsKey(name)) {
            classObject = this.classMap.get(name);
        }

        if (classObject == null) {
            for (final AddonClassLoader loader : this.loaders.values()) {
                try {
                    if ((classObject = loader.findClass(name, false)) != null) {
                        this.classMap.put(name, classObject);

                        break;
                    }
                } catch (final Exception ignored) {}
            }
        }

        return classObject;
    }

    /**
     * Creates the addons folder, loops through files, and loads all addons into the class path.
     *
     * @param depth defines how far we should check for jar/zip files
     * @return {@link AddonManager}
     * @throws IllegalStateException throws an exception if the directory cannot be created
     */
    public @NotNull AddonManager load(final int depth) throws IllegalStateException {
        try {
            Files.createDirectories(this.folder);
        } catch (final IOException exception) {
            throw new IllegalStateException(String.format("Could not create folder %s!", this.folder), exception);
        }

        final List<Path> addons = getFiles(this.folder, List.of(
                ".jar",
                ".zip"
        ), depth);

        addons.forEach(this::loadAddon);

        return this;
    }

    /**
     * Retrieves an instance of the addon by using the class object
     *
     * @param classObject the instance
     * @return {@code <T extends IAddon> Optional<T>}
     * @param <T> the extended class
     */
    public <T extends IAddon> Optional<T> getAddonInstance(@NotNull final Class<T> classObject) {
        return this.loaders.values().stream().map(AddonClassLoader::getAddon).filter(Objects::nonNull).filter(addon -> addon.getClass().equals(classObject)).map(classObject::cast).findAny();
    }

    /**
     * Retrieves an instance of the addon by name.
     *
     * @param name                     the name of the addon
     * @return {@code Optional<Addon>} an optional containing the addon instance
     */
    public Optional<IAddon> getAddonInstance(@NotNull final String name) {
        if (this.loaders.containsKey(name)) {
            return Optional.ofNullable(this.loaders.get(name).getAddon());
        }

        return Optional.empty();
    }

    /**
     * Reloads an addon.
     *
     * @param addon            the addon instance
     * @param <T>              the addon instance
     * @throws IllegalStateException throws an exception if the addon could not be reloaded.
     * @return {@code <T extends IAddon> IAddon}
     */
    public <T extends IAddon> IAddon reloadAddon(@NotNull final T addon) throws IllegalStateException {
        if (addon.isEnabled()) {
            addon.disable();
        }

        final Path path = addon.getLoader().getPath();

        IAddon foundKey;

        try (AddonClassLoader loader = this.loadAddon(path)) {
            final IAddon key = loader.getAddon();

            if (key != null) {
                key.enable(this.folder.resolve(key.getName()));
            }

            foundKey = key;
        } catch (final Exception exception) {
            throw new IllegalStateException(String.format("Could not reload addon %s!", addon), exception);
        }

        return foundKey;
    }

    /**
     * Unloads an addon.
     *
     * @param classObject      the addon instance
     * @param <T>              the class instance
     * @throws IllegalStateException throws an exception if the addon could not be reloaded.
     */
    public <T extends IAddon> void unloadAddon(@NotNull final Class<T> classObject) {
        getAddonInstance(classObject).ifPresent(this::unloadAddon);
    }

    /**
     * Reloads an addon.
     *
     * @param addon {@link IAddon}
     */
    public void reloadAddonConfig(@NotNull final IAddon addon) {
        addon.onDisable();
        addon.onEnable();
    }

    /**
     * Unloads an addon.
     *
     * @param addon {@link T}  the addon instance
     * @param <T>              the class instance
     * @throws IllegalStateException throws an exception if the addon is not found.
     */
    public <T extends IAddon> void unloadAddon(@NotNull final T addon) throws IllegalStateException {
        if (addon.isEnabled()) {
            addon.disable();
        }

        final AddonClassLoader classLoader = addon.getLoader();

        classLoader.setDisabling(true);
        classLoader.removeClasses();

        final String name = addon.getName();

        if (!this.loaders.containsKey(name)) {
            throw new IllegalStateException(String.format("Cannot find class loader by name %s, Panicking!", name));
        }

        classLoader.setDisabling(false);

        //noinspection EmptyTryBlock
        try (final AddonClassLoader loader = this.loaders.remove(name)) {

        } catch (final IOException ignored) {}
    }

    /**
     * Loads an addon by path.
     *
     * @param path the relative path object
     * @return {@link AddonClassLoader} the class loader
     * @throws IllegalStateException throws an exception if the configuration is invalid, or if an addon already exists with that name.
     */
    public @NotNull AddonClassLoader loadAddon(@NotNull final Path path) throws IllegalStateException {
        final Addon addon = getProperties(path);

        final String group = addon.getMain();
        final String name = addon.getName();

        if (group.isEmpty() || group.equals("N/A")) {
            throw new IllegalStateException(String.format("Addon %s does not have a group key.", group));
        }

        if (name.isEmpty() || name.equals("N/A")) {
            throw new IllegalStateException(String.format("Addon %s does not have a name key.", name));
        }

        if (this.loaders.containsKey(name)) {
            throw new IllegalStateException(String.format("Addon with the name %s already been loaded.", name));
        }

        if (this.classMap.containsKey(name)) {
            throw new IllegalStateException(String.format("Addon with the name %s main class has already been loaded.", name));
        }

        AddonClassLoader loader;

        try {
            loader = new AddonClassLoader(this, path, group, name);
        } catch (final Exception exception) {
            throw new IllegalStateException(String.format("Failed to load %s!", name), exception);
        }

        this.loaders.put(name, loader);

        return loader;
    }

    /**
     * Get a collection of addons, mapping to getAddon while filtering objects if they are null.
     *
     * @return {@code Collection<IAddon>} a list of addons
     */
    public @NotNull Collection<IAddon> getAddons() {
        return this.loaders.values().stream().map(AddonClassLoader::getAddon).filter(Objects::nonNull).toList();
    }

    /**
     * Enable all addons. Does NOT load any addons.
     *
     * @return {@link AddonManager}
     */
    public @NotNull AddonManager enableAddons() {
        this.loaders.values().stream().map(AddonClassLoader::getAddon).filter(Objects::nonNull).filter(addOn -> !addOn.isEnabled()).forEach(addon -> addon.enable(this.folder.resolve(addon.getName())));

        return this;
    }

    /**
     * Disable all addons. Does NOT unload them.
     *
     * @return {@link AddonManager}
     */
    public @NotNull AddonManager disableAddons() {
        this.loaders.values().stream().map(AddonClassLoader::getAddon).filter(Objects::nonNull).filter(IAddon::isEnabled).forEach(IAddon::disable);

        return this;
    }

    /**
     * Disables all addons, and purges the caches.
     */
    public void purge() {
        disableAddons();

        this.classMap.clear();
        this.loaders.clear();
    }

    /**
     * The path of the addon manager.
     *
     * @return {@link Path}
     */
    public @NotNull final Path getPath() {
        return this.path;
    }

    /**
     * Fetches properties using jar file and inputstreams.
     *
     * @param path the relative path
     * @return {@link Addon}
     */
    private @NotNull Addon getProperties(@NotNull final Path path) {
        final Properties properties = new Properties();

        try (final FileSystem entry = FileSystems.newFileSystem(path, (ClassLoader) null); final InputStream stream = Files.newInputStream(entry.getPath("addon.properties"))) {
            properties.load(stream);
        } catch (final IOException exception) {
            throw new IllegalStateException("Failed to load addon.properties!", exception);
        }

        return new Addon(properties);
    }

    /**
     * Retrieves a list of paths from the relative path, including the given extensions.
     * This method searches up to the specified depth within the directory structure.
     *
     * @param path the directory to scan for files
     * @param extensions the list of file extensions to be searched for (e.g., ".yml")
     * @param depth the maximum depth level to search within subdirectories
     * @return a list of files
     */
    private List<Path> getFiles(@NotNull final Path path, @NotNull final List<String> extensions, final int depth) {
        final List<Path> files = new ArrayList<>();

        if (Files.isDirectory(path)) { // check if resolved path is a folder to loop through!
            try {
                Files.walkFileTree(path, new HashSet<>(), Math.max(depth, 1), new SimpleFileVisitor<>() {
                    @Override
                    public @NotNull FileVisitResult visitFile(@NotNull final Path path, @NotNull final BasicFileAttributes attributes) {
                        final String name = path.getFileName().toString();

                        extensions.forEach(extension -> {
                            if (name.endsWith(extension)) {
                                files.add(path);
                            }
                        });

                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (final IOException exception) {
                throw new IllegalStateException("Failed to get a list of files", exception);
            }
        }

        return files;
    }
}