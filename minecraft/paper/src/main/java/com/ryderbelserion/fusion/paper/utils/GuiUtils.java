package com.ryderbelserion.fusion.paper.utils;

import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.paper.FusionPaper;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.inventory.CraftContainer;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

public class GuiUtils {

    private static final FusionPaper fusion = (FusionPaper) FusionProvider.getInstance();

    public static void updateTitle(@NotNull final Player player, @NotNull final String title, @NotNull final Map<String, String> placeholders) {
        final ServerPlayer entityPlayer = (ServerPlayer) ((CraftEntity) player).getHandle();

        final int containerId = entityPlayer.containerMenu.containerId;

        final MenuType<?> windowType = CraftContainer.getNotchInventoryType(player.getOpenInventory().getTopInventory());

        entityPlayer.connection.send(new ClientboundOpenScreenPacket(containerId, windowType, CraftChatMessage.fromJSON(JSONComponentSerializer.json().serialize(fusion.asComponent(player, title, placeholders)))));

        player.updateInventory();
    }

    public static void updateTitle(@NotNull final Player player, @NotNull final String title) {
        updateTitle(player, title, new HashMap<>());
    }
}