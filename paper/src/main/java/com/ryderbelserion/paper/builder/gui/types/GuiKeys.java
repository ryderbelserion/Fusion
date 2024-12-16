package com.ryderbelserion.paper.builder.gui.types;

import com.ryderbelserion.FusionSettings;
import io.papermc.paper.persistence.PersistentDataContainerView;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class GuiKeys {

    private static final Plugin plugin = FusionSettings.get().getPlugin();

    public GuiKeys() {
        throw new AssertionError();
    }

    public static NamespacedKey key = new NamespacedKey(plugin, "mf-gui");

    public static @NotNull String getUUID(final ItemStack itemStack) {
        return itemStack.getPersistentDataContainer().getOrDefault(key, PersistentDataType.STRING, "");
    }

    public static @NotNull NamespacedKey build(final String key) {
        return new NamespacedKey(plugin, key);
    }

    public static @NotNull ItemStack strip(final ItemStack itemStack) {
        final PersistentDataContainerView container = itemStack.getPersistentDataContainer();

        if (!container.has(key)) {
            return itemStack;
        }

        itemStack.editMeta(itemMeta -> itemMeta.getPersistentDataContainer().remove(key));

        return itemStack;
    }
}