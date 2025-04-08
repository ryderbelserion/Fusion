package com.ryderbelserion.fusion.paper.api.builder.gui;

import com.ryderbelserion.fusion.paper.api.builder.gui.types.StorageGui;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;

public final class StorageBuilder extends BaseGuiBuilder<StorageGui, StorageBuilder> {

    @NotNull
    @Override
    @Contract(" -> new")
    public StorageGui create() {
        final StorageGui gui = new StorageGui(createContainer(), getComponents());

        final Consumer<StorageGui> consumer = getConsumer();

        if (consumer != null) consumer.accept(gui);

        return gui;
    }
}