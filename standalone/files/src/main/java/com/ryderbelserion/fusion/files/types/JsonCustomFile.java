package com.ryderbelserion.fusion.files.types;

import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.interfaces.AbstractConfigurate;
import com.ryderbelserion.fusion.files.objects.FileData;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.jackson.JacksonConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;
import java.io.IOException;

@NullMarked
public class JsonCustomFile extends AbstractConfigurate<JacksonConfigurationLoader, BasicConfigurationNode> {

    public JsonCustomFile(final FileManager fileManager, final FileData fileData) {
        super(fileManager, fileData);

        final JacksonConfigurationLoader.Builder builder = JacksonConfigurationLoader.builder()
                .path(this.path);

        this.loader = builder.build();
    }

    @Override
    public <V> void write(final V value, final Object... args) {
        if (this.configuration == null) {
            throw new IllegalStateException("Configuration is null!");
        }

        try {
            this.configuration.node(args).set(value);
        } catch (final SerializationException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public <V> V read(final V value, final Object... args) {
        if (this.configuration == null) {
            throw new IllegalStateException("Configuration is null!");
        }

        try {
            return (V) this.configuration.node(args).get(value.getClass(), value);
        } catch (final SerializationException exception) {
            exception.printStackTrace();
        }

        return value;
    }

    @Override
    public BasicConfigurationNode loadConfig() throws IOException {
        if (this.loader == null) {
            throw new IOException("The configuration loader has not been initialized.");
        }

        return this.loader.load();
    }

    @Override
    public void saveConfig() throws IOException {
        if (this.loader == null) {
            throw new IOException("The configuration loader has not been initialized.");
        }

        this.loader.save(this.configuration);
    }
}