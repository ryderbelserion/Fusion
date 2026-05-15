package com.ryderbelserion.fusion.paper.builders.gui.interfaces;

import org.bukkit.event.Event;
import org.jspecify.annotations.NonNull;

@FunctionalInterface
public interface GuiAction<T extends Event> {

    void execute(@NonNull final T event);

}