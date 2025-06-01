package com.ryderbelserion.fusion.core.api.addons;

import com.ryderbelserion.fusion.core.api.addons.objects.Addon;
import com.ryderbelserion.fusion.core.api.addons.interfaces.IAddon;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.utils.FileUtils;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
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

    /**
     * The default constructor to create an instance of {@link AddonManager}
     *
     * @param path the addons folder
     */
    public AddonManager(@NotNull final Path path) {
        this.folder = path.resolve("addons");
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
                throw new FusionException(String.format("Class %s did not get removed.", object.getName()));
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
     * @return {@link AddonManager}
     * @throws FusionException throws an exception if the directory cannot be created
     */
    public @NotNull AddonManager load() throws FusionException {
        try {
            Files.createDirectories(this.folder);
        } catch (final IOException exception) {
            throw new FusionException(String.format("Could not create folder %s", this.folder), exception);
        }

        final List<Path> addons = FileUtils.getFiles(this.folder, List.of(
                ".jar",
                ".zip"
        ));

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
     * @throws FusionException throws an exception if the addon could not be reloaded.
     * @return {@code <T extends IAddon> IAddon}
     */
    public <T extends IAddon> IAddon reloadAddon(@NotNull final T addon) throws FusionException {
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
            throw new FusionException(String.format("Could not reload addon %s", addon), exception);
        }

        return foundKey;
    }

    /**
     * Unloads an addon.
     *
     * @param classObject      the addon instance
     * @param <T>              the class instance
     * @throws FusionException throws an exception if the addon could not be reloaded.
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
     * @throws FusionException throws an exception if the addon is not found.
     */
    public <T extends IAddon> void unloadAddon(@NotNull final T addon) throws FusionException {
        if (addon.isEnabled()) {
            addon.disable();
        }

        final AddonClassLoader classLoader = addon.getLoader();

        classLoader.setDisabling(true);
        classLoader.removeClasses();

        final String name = addon.getName();

        if (!this.loaders.containsKey(name)) {
            throw new FusionException(String.format("Cannot find class loader by name %s, Panicking!", name));
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
     * @throws FusionException throws an exception if the configuration is invalid, or if an addon already exists with that name.
     */
    public @NotNull AddonClassLoader loadAddon(@NotNull final Path path) throws FusionException {
        final Addon addon = getProperties(path);

        final String group = addon.getMain();
        final String name = addon.getName();

        if (group.isEmpty() || group.equals("N/A")) {
            throw new FusionException(String.format("Addon %s does not have a group key.", group));
        }

        if (name.isEmpty() || name.equals("N/A")) {
            throw new FusionException(String.format("Addon %s does not have a name key.", name));
        }

        if (this.loaders.containsKey(name)) {
            throw new FusionException(String.format("Addon with the name %s already been loaded.", name));
        }

        if (this.classMap.containsKey(name)) {
            throw new FusionException(String.format("Addon with the name %s main class has already been loaded.", name));
        }

        AddonClassLoader loader;

        try {
            loader = new AddonClassLoader(this, path, group, name);
        } catch (final Exception exception) {
            throw new FusionException(String.format("Failed to load %s", name), exception);
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
            throw new FusionException("Failed to load addon.properties", exception);
        }

        return new Addon(properties);
    }
}