package com.ryderbelserion.fusion.core.api.registry.types;

import com.ryderbelserion.fusion.core.api.objects.FileKey;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FileRegistry {

    private final Map<String, FileKey> registry = new HashMap<>();

    public FileRegistry() {
        register(".yml", "yaml");
        register(".yml", "jalu");
    }

    public FileRegistry register(String extension, String name) {
        this.registry.put(name, new FileKey(extension, name));

        return this;
    }

    public FileKey get(String name) {
        return this.registry.get(name);
    }

    public Map<String, FileKey> getRegistry() {
        return Collections.unmodifiableMap(this.registry);
    }
}