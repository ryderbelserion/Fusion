package com.ryderbelserion.fusion.paper.builders.gui.objects.border.interfaces;

import com.ryderbelserion.fusion.paper.builders.gui.objects.border.GuiFiller;
import org.jspecify.annotations.NonNull;

public interface CustomFiller {

    void fillRemaining(@NonNull final GuiFiller guiFiller);

    void fillBottom(@NonNull final GuiFiller guiFiller);

    void fillRight(@NonNull final GuiFiller guiFiller);

    void fillLeft(@NonNull final GuiFiller guiFiller);

    void fillBoth(@NonNull final GuiFiller guiFiller);

    void fillTop(@NonNull final GuiFiller guiFiller);

}