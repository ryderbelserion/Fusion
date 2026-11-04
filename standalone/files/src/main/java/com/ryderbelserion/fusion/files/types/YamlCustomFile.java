package com.ryderbelserion.fusion.files.types;

import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.interfaces.AbstractConfigurate;
import com.ryderbelserion.fusion.files.objects.FileData;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import java.io.IOException;

@NullMarked
public class YamlCustomFile extends AbstractConfigurate<Object, YamlConfigurationLoader, CommentedConfigurationNode> {

    public YamlCustomFile(final FileManager fileManager, final FileData fileData) {
        super(fileManager, fileData);

        final YamlConfigurationLoader.Builder builder = YamlConfigurationLoader.builder()
                .path(this.path);

        this.loader = builder.build();
    }

    @Override
    public void write(final Object value, final Object... args) {
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
    public CommentedConfigurationNode loadConfig() throws IOException {
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