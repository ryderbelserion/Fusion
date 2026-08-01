package com.ryderbelserion.fusion.paper.builders.items.types.plugins;

import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.builders.items.BaseItemBuilder;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;
import java.util.Optional;

@NullMarked
public abstract class ICustomItem {

    protected final FusionPaper fusion = (FusionPaper) FusionProvider.getInstance();

    protected final JavaPlugin plugin = this.fusion.getPlugin();

    protected final Server server = this.plugin.getServer();

    protected final PluginManager pluginManager = this.server.getPluginManager();

    protected final BaseItemBuilder builder;
    protected final boolean isEnabled;
    protected final String item;

    public ICustomItem(final BaseItemBuilder builder, final String item, final boolean isEnabled) {
        this.isEnabled = isEnabled;
        this.builder = builder;
        this.item = item;
    }

    public abstract Optional<ItemStack> getItemStack();

    public abstract ICustomItem init();

    public abstract String getImpl();

    public boolean isAvailable() {
        return false;
    }
}