package com.ryderbelserion.fusion;

import com.ryderbelserion.fusion.fabric.FusionFabric;
import com.ryderbelserion.fusion.fabric.builders.ItemBuilder;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.world.item.Items;
import java.nio.file.Path;

public class Fusion implements DedicatedServerModInitializer {

    private FusionFabric fabric;

    @Override
    public void onInitializeServer() {
        this.fabric = new FusionFabric(null, Path.of("beans"), consumer -> {});

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            final ItemBuilder itemBuilder = new ItemBuilder(Items.DIAMOND).setName("Fancy Diamonds!").setAmount(32);

            itemBuilder.addInventory(handler.player);
        });
    }
}