package com.ryderbelserion.fusion;

import com.ryderbelserion.fusion.fabric.builders.ItemBuilder;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.world.item.Items;

public class Fusion implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            final ItemBuilder itemBuilder = new ItemBuilder(Items.DIAMOND).setName("Fancy Diamonds!").setAmount(32);

            itemBuilder.addInventory(handler.player);
        });
    }
}