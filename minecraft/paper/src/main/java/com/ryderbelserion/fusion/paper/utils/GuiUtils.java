package com.ryderbelserion.fusion.paper.utils;

import com.ryderbelserion.fusion.api.FusionProvider;
import com.ryderbelserion.fusion.paper.FusionPaper;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.CraftContainer;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.jspecify.annotations.NullMarked;
import java.util.HashMap;
import java.util.Map;

@NullMarked
public class GuiUtils {

    private static final FusionPaper fusion = (FusionPaper) FusionProvider.api();

    public static String updateTitle(final Player player, final InventoryView inventory, final String origin, final Map<String, String> placeholders) {
        final ServerPlayer entityPlayer = (ServerPlayer) ((CraftHumanEntity) player).getHandle();

        final int containerId = entityPlayer.containerMenu.containerId;

        final MenuType<?> windowType = CraftContainer.getNotchInventoryType(inventory.getTopInventory());

        final String title = fusion.replacePlaceholders(origin, placeholders);

        entityPlayer.connection.send(new ClientboundOpenScreenPacket(containerId, windowType, CraftChatMessage.fromJSON(JSONComponentSerializer.json().serialize(fusion.asComponent(player, title)))));
        entityPlayer.containerMenu.sendAllDataToRemote();

        return title;
    }

    public static String updateTitle(final Player player, final InventoryView inventory, final String origin) {
        return updateTitle(player, inventory, origin, new HashMap<>());
    }
}