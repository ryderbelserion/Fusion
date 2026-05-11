package com.ryderbelserion.fusion.kyori.commands.api.senders;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.FusionKey;
import com.ryderbelserion.fusion.core.api.FusionProvider;
import net.kyori.adventure.key.Key;

public class MetaKeys {

    private static final FusionCore fusion = FusionProvider.getInstance();

    public static final String namespace = fusion.getNamespace();

    public static final FusionKey must_be_console_sender = FusionKey.key(namespace, "must_be_console_sender");

    public static final FusionKey must_be_player = FusionKey.key(namespace, "must_be_player");

}