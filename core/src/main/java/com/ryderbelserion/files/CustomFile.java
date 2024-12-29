package com.ryderbelserion.files;

import com.ryderbelserion.FusionLayout;
import com.ryderbelserion.FusionProvider;
import com.ryderbelserion.api.enums.FileType;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.spongepowered.configurate.BasicConfigurationNode;
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

    public CustomFile(final File file, final boolean isDynamic, final String extension) {
        this.effectiveName = file.getName().replace(extension, "");
        this.isDynamic = isDynamic;
        this.file = file;
    }

    public String getStringValueWithDefault(final String defaultValue, final Object... path) {
        return null;
    }

    public String getStringValue(final Object... path) {
        return null;
    }

    public boolean getBooleanValueWithDefault(final boolean defaultValue, final Object... path) {
        return false;
    }

    public boolean getBooleanValue(final Object... path) {
        return false;
    }

    public double getDoubleValueWithDefault(final double defaultValue, final Object... path) {
        return 0;
    }

    public double getDoubleValue(final Object... path) {
        return 0;
    }

    public long getLongValueWithDefault(final long defaultValue, final Object... path) {
        return 0;
    }

    public long getLongValue(final Object... path) {
        return 0;
    }

    public int getIntValueWithDefault(final int defaultValue, final Object... path) {
        return 0;
    }

    public int getIntValue(final Object... path) {
        return 0;
    }

    public List<String> getStringList(final Object... path) {
        return null;
    }

    public abstract CustomFile<T> loadConfiguration();

    public abstract CustomFile<T> saveConfiguration();

    public CustomFile<T> deleteConfiguration() {
        final File file = getFile();

        if (file != null && file.exists() && file.delete()) {
            if (this.isVerbose) {
                this.logger.warn("Successfully deleted {}", getFileName());
            }
        }

        return this;
    }

    public abstract FileType getFileType();

    public CommentedConfigurationNode getConfigurationNode() {
        return null;
    }

    public BasicConfigurationNode getBasicConfigurationNode() {
        return null;
    }

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