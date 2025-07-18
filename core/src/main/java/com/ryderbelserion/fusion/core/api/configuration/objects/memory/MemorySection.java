package com.ryderbelserion.fusion.core.api.configuration.objects.memory;

import com.ryderbelserion.fusion.core.api.configuration.interfaces.IConfiguration;
import com.ryderbelserion.fusion.core.api.configuration.interfaces.IConfigurationSection;
import com.ryderbelserion.fusion.core.api.configuration.objects.sections.SectionPathData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class MemorySection implements IConfigurationSection {

    protected final Map<String, SectionPathData> map = new LinkedHashMap<>();

    private final IConfigurationSection parent;
    private final IConfiguration root;
    private final String effectivePath;
    private final String path;

    public MemorySection(@NotNull final IConfigurationSection parent, @NotNull final String path) {
        this.effectivePath = createPath(parent, path);
        this.root = parent.getRoot();
        this.parent = parent;
        this.path = path;
    }

    public MemorySection() {
        this.root = (IConfiguration) this;
        this.effectivePath = "";
        this.parent = null;
        this.path = "";
    }

    @Override
    public @NotNull String getEffectivePath() {
        return this.effectivePath;
    }

    @Override
    public @NotNull String getPath() {
        return this.path;
    }

    @Override
    public @NotNull Set<String> getKeys(final boolean deep) {
        final Set<String> result = new LinkedHashSet<>();

        final IConfiguration root = getRoot();

        if (root != null && root.options().copyDefaults()) {
            final IConfigurationSection defaults = getDefaultSection();

            if (defaults != null) {
                result.addAll(defaults.getKeys(deep));
            }
        }

        mapChildrenKeys(result, this, deep);

        return result;
    }

    @Override
    public @NotNull Map<String, Object> getValues(final boolean deep) {
        final Map<String, Object> result = new LinkedHashMap<>();

        final IConfiguration root = getRoot();

        if (root != null && root.options().copyDefaults()) {
            final IConfigurationSection defaults = getDefaultSection();

            if (defaults != null) {
                result.putAll(defaults.getValues(deep));
            }
        }

        mapChildrenValues(result, this, deep);

        return result;
    }

    @Override
    @Nullable
    public IConfigurationSection getDefaultSection() {
        final IConfiguration root = getRoot();
        final IConfiguration defaults = root == null ? null : root.defaults();

        if (defaults == null) return null;

        final String effectivePath = getEffectivePath();

        if (!defaults.isConfigurationSection(effectivePath)) return null;

        return defaults.getConfigurationSection(effectivePath);
    }

    @Override
    public void set(@NotNull final String path, @Nullable final Object value) {

    }

    @Override
    public @Nullable Object get(@NotNull final String path) {
        return null;
    }

    @Override
    public boolean isConfigurationSection(@NotNull final String path) {
        return false;
    }

    @Override
    public @Nullable IConfigurationSection getConfigurationSection(@NotNull final String path) {
        Object val = get(path, null);

        if (val != null) {
            return (val instanceof IConfigurationSection) ? (IConfigurationSection) val : null;
        }

        val = get(path, getDefault(path));

        return (val instanceof IConfigurationSection) ? createSection(path) : null;
    }

    public Object get(@NotNull final String path, @Nullable final Object defaultValue) {
        return null;
    }

    public String getDefault(@NotNull final String path) {
        return "";
    }

    public IConfigurationSection createSection(@NotNull final String path) {
        return new MemorySection(this, path);
    }

    @Override
    public @Nullable IConfigurationSection getParent() {
        return this.parent;
    }

    @Override
    public @Nullable IConfiguration getRoot() {
        return this.root;
    }

    public String createPath(@NotNull final IConfigurationSection section, @Nullable final String key) {
        return createPath(section, key, section.getRoot());
    }

    public String createPath(@NotNull final IConfigurationSection section, @Nullable final String key, @Nullable final IConfigurationSection relative) {
        final IConfiguration root = section.getRoot();

        if (root == null) {
            return "";
        }

        final char separator = root.options().pathSeparator();

        final StringBuilder result = new StringBuilder();

        for (IConfigurationSection parent = section; (parent != null) && (parent != relative); parent = parent.getParent()) {
            if (!result.isEmpty()) {
                result.insert(0, separator);
            }

            result.insert(0, parent.getPath());
        }

        if ((key != null) && (!key.isEmpty())) {
            if (!result.isEmpty()) {
                result.append(separator);
            }

            result.append(key);
        }

        return result.toString();
    }

    protected void mapChildrenKeys(@NotNull Set<String> output, @NotNull IConfigurationSection section, boolean deep) {
        if (section instanceof MemorySection memorySection) {
            for (Map.Entry<String, SectionPathData> entry : memorySection.map.entrySet()) {
                output.add(createPath(section, entry.getKey(), this));

                if ((deep) && (entry.getValue().getData() instanceof IConfigurationSection sectionData)) {
                    mapChildrenKeys(output, sectionData, deep);
                }
            }

            return;
        }

        final Set<String> keys = section.getKeys(deep);

        for (final String key : keys) {
            output.add(createPath(section, key, this));
        }
    }

    protected void mapChildrenValues(@NotNull Map<String, Object> output, @NotNull IConfigurationSection section, boolean deep) {
        if (section instanceof MemorySection memorySection) {
            for (Map.Entry<String, SectionPathData> entry : memorySection.map.entrySet()) {
                // Because of the copyDefaults call potentially copying out of order, we must remove and then add in our saved order
                // This means that default values we haven't set end up getting placed first
                // See SPIGOT-4558 for an example using spigot.yml - watch subsections move around to default order
                final String childPath = createPath(section, entry.getKey(), this);

                output.remove(childPath);

                output.put(childPath, entry.getValue().getData());

                if (entry.getValue().getData() instanceof IConfigurationSection sectionData) {
                    if (deep) {
                        mapChildrenValues(output, sectionData, deep);
                    }
                }
            }

            return;
        }

        final Map<String, Object> values = section.getValues(deep);

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            output.put(createPath(section, entry.getKey(), this), entry.getValue());
        }
    }
}