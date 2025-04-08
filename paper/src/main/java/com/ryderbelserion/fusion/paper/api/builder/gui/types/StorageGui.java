package com.ryderbelserion.fusion.paper.api.builder.gui.types;

import com.ryderbelserion.fusion.paper.api.builder.gui.enums.GuiComponent;
import com.ryderbelserion.fusion.paper.api.builder.gui.interfaces.GuiContainer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StorageGui extends BaseGui {

    public StorageGui(@NotNull final GuiContainer guiContainer, @NotNull final Set<GuiComponent> components) {
        super(guiContainer, components);
    }
    
    @NotNull
    public Map<@NotNull Integer, @NotNull ItemStack> addItem(@NotNull final ItemStack... items) {
        return Collections.unmodifiableMap(getInventory().addItem(items));
    }
    
    public Map<@NotNull Integer, @NotNull ItemStack> addItem(@NotNull final List<ItemStack> items) {
        return addItem(items.toArray(new ItemStack[0]));
    }
}