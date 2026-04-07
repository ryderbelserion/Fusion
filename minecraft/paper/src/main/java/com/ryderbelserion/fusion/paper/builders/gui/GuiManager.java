package com.ryderbelserion.fusion.paper.builders.gui;

import com.ryderbelserion.fusion.paper.builders.gui.objects.border.interfaces.CustomFiller;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GuiManager {

    private final Map<String, CustomFiller> fillers = new HashMap<>();

    public void addCustomFiller(@NotNull final String name, @NotNull final CustomFiller filler) {
        this.fillers.put(name, filler);
    }

    public void removeCustomFiller(@NotNull final String name) {
        this.fillers.remove(name);
    }

    public @NotNull final Optional<CustomFiller> getFiller(@NotNull final String name) {
        return Optional.ofNullable(this.fillers.get(name));
    }
}