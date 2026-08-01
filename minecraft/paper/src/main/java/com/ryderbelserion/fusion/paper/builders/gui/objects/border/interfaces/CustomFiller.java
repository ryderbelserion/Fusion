package com.ryderbelserion.fusion.paper.builders.gui.objects.border.interfaces;

import com.ryderbelserion.fusion.paper.builders.gui.objects.border.GuiFiller;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface CustomFiller {

    void fillRemaining(final GuiFiller guiFiller);

    void fillBottom(final GuiFiller guiFiller);

    void fillRight(final GuiFiller guiFiller);

    void fillLeft(final GuiFiller guiFiller);

    void fillBoth(final GuiFiller guiFiller);

    void fillTop(final GuiFiller guiFiller);

}