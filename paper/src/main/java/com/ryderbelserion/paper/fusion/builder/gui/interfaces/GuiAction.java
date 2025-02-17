package com.ryderbelserion.paper.fusion.builder.gui.interfaces;

import org.bukkit.event.Event;

@FunctionalInterface
public interface GuiAction<T extends Event> {

    void execute(final T event);

}