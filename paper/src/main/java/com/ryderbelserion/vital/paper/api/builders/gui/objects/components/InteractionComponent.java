package com.ryderbelserion.vital.paper.api.builders.gui.objects.components;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Controls what kind of interaction can happen inside a menu.
 *
 * @author SecretX
 * @since 0.0.1
 */
public enum InteractionComponent {

    /**
     * Prevent item placement
     */
    PREVENT_ITEM_PLACE,
    /**
     * Prevent item take
     */
    PREVENT_ITEM_TAKE,
    /**
     * Prevent item swap
     */
    PREVENT_ITEM_SWAP,
    /**
     * Prevent item drop
     */
    PREVENT_ITEM_DROP,
    /**
     * Prevent other actions
     */
    PREVENT_OTHER_ACTIONS;

    /**
     * Set of values
     */
    public static final Set<InteractionComponent> VALUES = Collections.unmodifiableSet(EnumSet.allOf(InteractionComponent.class));

}