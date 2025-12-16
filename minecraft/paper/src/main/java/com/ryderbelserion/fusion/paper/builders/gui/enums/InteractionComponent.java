package com.ryderbelserion.fusion.paper.builders.gui.enums;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public enum InteractionComponent {

    PREVENT_ITEM_PLACE,
    PREVENT_ITEM_TAKE,
    PREVENT_ITEM_SWAP,
    PREVENT_ITEM_DROP,
    PREVENT_OTHER_ACTIONS;

    public static final Set<InteractionComponent> VALUES = Collections.unmodifiableSet(EnumSet.allOf(InteractionComponent.class));

}