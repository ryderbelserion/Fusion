package com.ryderbelserion.fusion.paper.api.builders.gui.interfaces;

import org.bukkit.event.Event;

@FunctionalInterface
public interface GuiAction<T extends Event> {

    void execute(final T event);

}