package com.ryderbelserion.fusion.hytale.interfaces;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.receiver.IMessageReceiver;
import org.jspecify.annotations.NullMarked;
import java.util.Map;

@NullMarked
public interface IFusionHytale {

    Message asMessage(final IMessageReceiver receiver, final String message, final Map<String, String> placeholders);

    Message asMessage(final IMessageReceiver receiver, final String message);

}