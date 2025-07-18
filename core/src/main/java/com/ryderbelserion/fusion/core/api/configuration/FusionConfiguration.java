package com.ryderbelserion.fusion.core.api.configuration;

import com.ryderbelserion.fusion.core.api.configuration.objects.Configuration;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.representer.Representer;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FusionConfiguration extends Configuration {

    private final DumperOptions dumperOptions;
    private final LoaderOptions loaderOptions;
    private final SafeConstructor constructor;
    private final Representer representer;

    private final Yaml yaml;

    public FusionConfiguration() {
        this.dumperOptions = new DumperOptions();
        this.dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        this.loaderOptions = new LoaderOptions();
        this.loaderOptions.setMaxAliasesForCollections(Integer.MAX_VALUE);
        this.loaderOptions.setCodePointLimit(Integer.MAX_VALUE);
        this.loaderOptions.setNestingDepthLimit(100);

        this.constructor = new SafeConstructor(this.loaderOptions);
        this.representer = new Representer(this.dumperOptions);
        this.representer.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        this.yaml = new Yaml(this.constructor, this.representer, this.dumperOptions, this.loaderOptions);
    }

    public FusionConfiguration loadConfiguration(@NotNull final Path path) {
        try (final InputStream inputStream = Files.newInputStream(path, StandardOpenOption.READ, StandardOpenOption.CREATE_NEW)) {
            this.yaml.load(inputStream);
        } catch (final IOException exception) {
            exception.printStackTrace();
        }

        return this;
    }
}