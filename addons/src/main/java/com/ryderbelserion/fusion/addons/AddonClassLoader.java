package com.ryderbelserion.fusion.addons;

import com.ryderbelserion.fusion.addons.interfaces.IAddon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles class loaders related to addons.
 */
public class AddonClassLoader extends URLClassLoader {

    private final Map<String, Class<?>> classes = new ConcurrentHashMap<>();

    private final AddonManager manager;
    private final IAddon addon;
    private final Path path;

    private boolean isDisabling;
    private String name;

    /**
     * Constructs an instance of the addon class loader
     *
     * @param manager the addon manager
     * @param path the path for the addon's directory
     * @param group the entry point
     * @param name the name of the addon
     * @throws IllegalStateException throws {@link IllegalStateException}
     * @throws MalformedURLException throws {@link MalformedURLException}
     */
    public AddonClassLoader(@NotNull final AddonManager manager, @NotNull final Path path, @NotNull final String group, @NotNull final String name) throws IllegalStateException, MalformedURLException {
        super(new URL[]{path.toUri().toURL()}, manager.getClass().getClassLoader());

        this.manager = manager;
        this.name = name.isBlank() ? path.getFileName().toString() : name;
        this.path = path;

        Class<?> mainClass;

        try {
            mainClass = Class.forName(group, true, this);

            this.classes.put(mainClass.getName(), mainClass);
        } catch (final ClassNotFoundException exception) {
            throw new IllegalStateException(String.format("Could not find main class for addon %s!", name), exception);
        }

        Class<? extends IAddon> addonClass;

        try {
            addonClass = mainClass.asSubclass(IAddon.class);
        } catch (final Exception exception) {
            throw new IllegalStateException(String.format("%s does not implement iAddon!", group), exception);
        }

        try {
            this.addon = addonClass.getDeclaredConstructor().newInstance();
            this.addon.setLoader(this);
            this.addon.setName(this.name = name);
            this.addon.setGroup(group);
        } catch (final Exception exception) {
            throw new IllegalStateException(String.format("Failed to load main class for addon %s!", name), exception);
        }
    }

    /**
     * Finds and loads the class with the specified name from the URL search
     * path. Any URLs referring to JAR files are loaded and opened as needed
     * until the class is found.
     *
     * @param     name the name of the class
     * @return    the resulting class
     * @throws    ClassNotFoundException if the class could not be found,
     *            or if the loader is closed.
     * @throws    NullPointerException if {@code name} is {@code null}.
     */
    @Override
    protected Class<?> findClass(@NotNull final String name) throws ClassNotFoundException {
        return findClass(name, true);
    }

    /**
     * Finds and loads the class with the specified name from the URL search
     * path. Any URLs referring to JAR files are loaded and opened as needed
     * until the class is found.
     *
     * @param     name the name of the class
     * @param     isGlobal true or false
     * @return    the resulting class
     * @throws    ClassNotFoundException if the class could not be found,
     *            or if the loader is closed.
     * @throws    NullPointerException if {@code name} is {@code null}.
     */
    public Class<?> findClass(@NotNull final String name, final boolean isGlobal) throws ClassNotFoundException {
        if (this.isDisabling()) {
            throw new ClassNotFoundException("This class loader is disabling!");
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

    /**
     * Removes all classes from the class path.
     */
    public void removeClasses() {
        if (!this.isDisabling()) {
            throw new IllegalStateException("Cannot remove class when the loader isn't disabling!");
        }

        this.manager.removeAll(this.classes);
        this.classes.clear();
    }

    /**
     * Sets the disable status of the addon.
     *
     * @param isDisabling true or false
     */
    public void setDisabling(final boolean isDisabling) {
        this.isDisabling = isDisabling;
    }

    /**
     * Checks if the addon is disabling.
     *
     * @return true or false
     */
    public boolean isDisabling() {
        return this.isDisabling;
    }

    /**
     * Fetches an instance of {@link AddonManager}.
     *
     * @return the instance of {@link AddonManager}
     */
    public @NotNull AddonManager getManager() {
        return this.manager;
    }

    /**
     * Fetches an instance of {@link IAddon}.
     *
     * @return the instance of {@link IAddon}
     */
    public @Nullable IAddon getAddon() {
        return this.addon;
    }

    /**
     * The name of the class or addon.
     *
     * @return the name of the class or addon
     */
    public @NotNull String getName() {
        return this.name;
    }

    /**
     * The path of the class or addon.
     *
     * @return the path of the class or addon
     */
    public @NotNull Path getPath() {
        return this.path;
    }
}