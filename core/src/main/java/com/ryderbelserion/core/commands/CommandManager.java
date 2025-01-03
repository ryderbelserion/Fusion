package com.ryderbelserion.core.commands;

import com.ryderbelserion.core.FusionLayout;
import com.ryderbelserion.core.FusionProvider;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

public class CommandManager {

    private final FusionLayout api = FusionProvider.get();

    private final ComponentLogger logger = this.api.getLogger();
}