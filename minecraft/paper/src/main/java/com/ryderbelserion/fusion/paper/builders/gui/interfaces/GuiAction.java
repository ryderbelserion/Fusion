package com.ryderbelserion.fusion.paper.builders.gui.interfaces;

import org.bukkit.event.Event;
import org.jspecify.annotations.NullMarked;

@FunctionalInterface
@NullMarked
public interface GuiAction<T extends Event> {

    void execute(final T event);

}