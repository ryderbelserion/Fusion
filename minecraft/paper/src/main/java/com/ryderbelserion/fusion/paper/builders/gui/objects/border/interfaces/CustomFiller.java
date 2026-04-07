package com.ryderbelserion.fusion.paper.builders.gui.objects.border.interfaces;

import com.ryderbelserion.fusion.paper.builders.gui.objects.border.GuiFiller;
import org.jetbrains.annotations.NotNull;

public interface CustomFiller {

    void fillRemaining(@NotNull final GuiFiller guiFiller);

    void fillBottom(@NotNull final GuiFiller guiFiller);

    void fillRight(@NotNull final GuiFiller guiFiller);

    void fillLeft(@NotNull final GuiFiller guiFiller);

    void fillBoth(@NotNull final GuiFiller guiFiller);

    void fillTop(@NotNull final GuiFiller guiFiller);

}