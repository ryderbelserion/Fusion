package com.ryderbelserion.fusion.paper.builders.gui;

import com.ryderbelserion.fusion.paper.builders.gui.objects.border.interfaces.CustomFiller;
import org.jspecify.annotations.NullMarked;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@NullMarked
public class GuiManager {

    private final Map<String, CustomFiller> fillers = new HashMap<>();

    public void addCustomFiller(final String name, final CustomFiller filler) {
        this.fillers.put(name, filler);
    }

    public void removeCustomFiller(final String name) {
        this.fillers.remove(name);
    }

    public final Optional<CustomFiller> getFiller(final String name) {
        return Optional.ofNullable(this.fillers.get(name));
    }
}