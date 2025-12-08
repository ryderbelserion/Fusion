package com.ryderbelserion.fusion.core.api.interfaces.permissions;

import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;

public interface IPermission {

    void init();

    void stop();

    boolean hasPermission(@NotNull final String node, @NotNull final Audience audience);

    boolean hasPermission(@NotNull final Audience audience);

    String getNode();

    int getChildren();

}