package com.ryderbelserion.files;

import com.ryderbelserion.FusionLayout;
import com.ryderbelserion.FusionProvider;
import com.ryderbelserion.api.enums.FileType;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.io.File;
import java.util.List;

public abstract class CustomFile<T extends CustomFile<T>> {

    protected final FusionLayout api = FusionProvider.get();

    protected final ComponentLogger logger = this.api.getLogger();

    protected final boolean isVerbose = this.api.isVerbose();

    private final String effectiveName;
    private final boolean isDynamic;
    private final File file;

    public CustomFile(final File file, final boolean isDynamic) {
        this.effectiveName = file.getName().replace(".yml", "");
        this.isDynamic = isDynamic;
        this.file = file;
    }

    public abstract String getStringValueWithDefault(final String defaultValue, final Object... path);

    public abstract String getStringValue(final Object... path);

    public abstract boolean getBooleanValueWithDefault(final boolean defaultValue, final Object... path);

    public abstract boolean getBooleanValue(final Object... path);

    public abstract double getDoubleValueWithDefault(final double defaultValue, final Object... path);

    public abstract double getDoubleValue(final Object... path);

    public abstract long getLongValueWithDefault(final long defaultValue, final Object... path);

    public abstract long getLongValue(final Object... path);

    public abstract int getIntValueWithDefault(final int defaultValue, final Object... path);

    public abstract int getIntValue(final Object... path);

    public abstract List<String> getStringList(final Object... path);

    public abstract CustomFile<T> loadConfiguration();

    public abstract CustomFile<T> saveConfiguration();

    public abstract FileType getFileType();

    public abstract CommentedConfigurationNode getConfigurationNode();

    public boolean isDynamic() {
        return this.isDynamic;
    }

    public CustomFile<T> getInstance() {
        return this;
    }

    public String getEffectiveName() {
        return this.effectiveName;
    }

    public String getFileName() {
        return this.file.getName();
    }

    public boolean isConfigurationLoaded() {
        return this.file.exists();
    }

    public File getFile() {
        return this.file;
    }
}