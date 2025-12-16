package com.ryderbelserion.fusion.paper.builders.gui.interfaces;

import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface GuiAction<T extends Event> {

    void execute(@NotNull final T event);

}