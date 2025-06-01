package com.ryderbelserion.fusion.core.api.addons;

import com.ryderbelserion.fusion.core.api.addons.interfaces.IAddon;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AddonClassLoader extends URLClassLoader {

    private final Map<String, Class<?>> classes = new ConcurrentHashMap<>();

    private final AddonManager manager;
    private final IAddon addon;
    private final Path path;

    private boolean isDisabling;
    private String name;

    public AddonClassLoader(@NotNull final AddonManager manager, @NotNull final Path path, @NotNull final String group, @NotNull final String name) throws FusionException, MalformedURLException {
        super(new URL[]{path.toUri().toURL()}, manager.getClass().getClassLoader());

        this.manager = manager;
        this.name = !name.isBlank() ? name : path.getFileName().toString();
        this.path = path;

        Class<?> mainClass;

        try {
            mainClass = Class.forName(group, true, this);

            this.classes.put(mainClass.getName(), mainClass);
        } catch (final ClassNotFoundException exception) {
            throw new FusionException("Could not find main class for addon: " + name, exception);
        }

        Class<? extends IAddon> addonClass;

        try {
            addonClass = mainClass.asSubclass(IAddon.class);
        } catch (final Exception exception) {
            throw new FusionException(group + " does not implement iAddon.", exception);
        }

        try {
            this.addon = addonClass.getDeclaredConstructor().newInstance();
            this.addon.setLoader(this);
            this.addon.setName(this.name = name);
        } catch (final Exception exception) {
            throw new FusionException("Failed to load main class for addon: " + name + ".", exception);
        }
    }

    @Override
    protected Class<?> findClass(@NotNull final String name) throws ClassNotFoundException {
        return findClass(name, true);
    }

    public Class<?> findClass(@NotNull final String name, final boolean isGlobal) throws ClassNotFoundException {
        if (this.isDisabling()) {
            throw new ClassNotFoundException("This class loader is disabling.");
        }

        if (this.classes.containsKey(name)) {
            return this.classes.get(name);
        }

        Class<?> classObject = null;

        if (isGlobal) {
            classObject = this.manager.findClass(name);
        }

        if (classObject == null) {
            classObject = super.findClass(name);

            if (classObject != null) {
                this.manager.setClass(name, classObject);
            }
        }

        return classObject;
    }

    public void removeClasses() {
        if (!this.isDisabling()) {
            throw new FusionException("Cannot remove class when the loader isn't disabling...");
        }

        this.manager.removeAll(this.classes);
        this.classes.clear();
    }

    public void setDisabling(final boolean isDisabling) {
        this.isDisabling = isDisabling;
    }

    public boolean isDisabling() {
        return this.isDisabling;
    }

    public @NotNull AddonManager getManager() {
        return this.manager;
    }

    public @Nullable IAddon getAddon() {
        return this.addon;
    }

    public @NotNull String getName() {
        return this.name;
    }

    public @NotNull Path getPath() {
        return this.path;
    }
}