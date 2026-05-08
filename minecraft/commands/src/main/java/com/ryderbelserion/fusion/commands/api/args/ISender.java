package com.ryderbelserion.fusion.commands.api.args;

import com.ryderbelserion.fusion.commands.api.args.objects.SenderData;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;

public interface ISender<T extends Audience, D> {

    SenderData<T> convert(@NotNull final D sender);

    Class<D> getSource();

    Class<T> getSender();

}