package com.ryderbelserion.fusion.paper.api.events;

import com.ryderbelserion.fusion.core.api.events.interfaces.IEvent;
import org.bukkit.event.Listener;

@FunctionalInterface
public interface IPaperEvent extends IEvent, Listener {}