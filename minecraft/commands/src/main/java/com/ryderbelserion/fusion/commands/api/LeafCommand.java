package com.ryderbelserion.fusion.commands.api;

import com.ryderbelserion.fusion.commands.annotations.subs.Leaf;
import org.jetbrains.annotations.NotNull;

public class LeafCommand {

    private final String leaf;

    public LeafCommand(@NotNull final Leaf leaf) {
        this.leaf = leaf.value();
    }

    public @NotNull final String getLeaf() {
        return this.leaf;
    }
}