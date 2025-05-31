package com.ryderbelserion.fusion.core.api.addons;

import com.ryderbelserion.fusion.core.api.addons.objects.Addon;
import com.ryderbelserion.fusion.core.api.addons.objects.AddonFilter;
import com.ryderbelserion.fusion.core.api.addons.interfaces.IAddon;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AddonManager {

    private final Map<String, Class<?>> classMap = new ConcurrentHashMap<>();
    private final Map<String, AddonClassLoader> loaders = new ConcurrentHashMap<>();
    private final Path folder;

    public AddonManager(@NotNull final Path path) {
        this.folder = path.resolve("addons");
    }

    public void removeAll(@NotNull final Map<String, Class<?>> classes) {
        classes.keySet().forEach(this.classMap::remove);

        for (final Class<?> object : classes.values()) {
            if (this.classMap.containsValue(object)) {
                throw new FusionException(String.format("Class %s did not get removed.", object.getName()));
            }
        }
    }

    public void setClass(@NotNull final String name, @NotNull final Class<?> classObject) {
        this.classMap.put(name, classObject);
    }

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

    public AddonManager load() throws FusionException {
        try {
            Files.createDirectories(this.folder);
        } catch (final IOException exception) {
            throw new FusionException("Could not create folder: " + this.folder, exception);
        }

        final File[] files = this.folder.toFile().listFiles(new AddonFilter());

        if (files == null) {
            return this;
        }

        Arrays.asList(files).forEach(this::loadAddon);

        return this;
    }

    public <T extends IAddon> Optional<T> getAddonInstance(@NotNull final Class<T> classObject) {
        return this.loaders.values().stream().map(AddonClassLoader::getAddon).filter(Objects::nonNull).filter(addon -> addon.getClass().equals(classObject)).map(classObject::cast).findAny();
    }

    public Optional<IAddon> getAddonInstance(@NotNull final String Name) {
        if (this.loaders.containsKey(Name)) {
            return Optional.ofNullable(this.loaders.get(Name).getAddon());
        }

        return Optional.empty();
    }

    public <T extends IAddon> IAddon reloadAddon(@NotNull final T addon) throws FusionException {
        if (addon.isEnabled()) {
            addon.disable();
        }

        final File file = addon.getLoader().getFile();

        IAddon foundKey;

        try (AddonClassLoader loader = this.loadAddon(file)) {
            final IAddon key = loader.getAddon();

            if (key != null) {
                key.enable(this.folder.resolve(key.getName()));
            }

            foundKey = key;
        } catch (final Exception exception) {
            throw new FusionException("Could not reload addon: " + addon, exception);
        }

        return foundKey;
    }

    public <T extends IAddon> void unloadAddon(@NotNull final Class<T> classObject) {
        getAddonInstance(classObject).ifPresent(this::unloadAddon);
    }

    public void reloadAddonConfig(@NotNull final IAddon addon) {
        addon.onDisable();
        addon.onEnable();
    }

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

    public AddonClassLoader loadAddon(@NotNull final File file) throws FusionException {
        final Addon addon = getProperties(file);

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
            loader = new AddonClassLoader(this, file, group, name);
        } catch (final Exception exception) {
            throw new FusionException("Failed to load " + name, exception);
        }

        this.loaders.put(name, loader);

        return loader;
    }

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

    public void purge() {
        disableAddons();

        this.classMap.clear();
        this.loaders.clear();
    }

    private @NotNull Addon getProperties(@NotNull final File file) {
        final Properties properties = new Properties();

        try (final JarFile entry = new JarFile(file)) {
            final JarEntry key = entry.getJarEntry("addon.properties");

            try (final InputStream stream = entry.getInputStream(key)) {
                properties.load(stream);
            }
        } catch (final IOException exception) {
            throw new FusionException("Failed to load addon.properties", exception);
        }

        return new Addon(properties);
    }
}