package com.ryderbelserion.fusion.paper.builders.gui;

import com.ryderbelserion.fusion.paper.builders.gui.objects.border.interfaces.CustomFiller;
import org.jspecify.annotations.NonNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GuiManager {

    private final Map<String, CustomFiller> fillers = new HashMap<>();

    public void addCustomFiller(@NonNull final String name, @NonNull final CustomFiller filler) {
        this.fillers.put(name, filler);
    }

    public void removeCustomFiller(@NonNull final String name) {
        this.fillers.remove(name);
    }

    public @NonNull final Optional<CustomFiller> getFiller(@NonNull final String name) {
        return Optional.ofNullable(this.fillers.get(name));
    }
}