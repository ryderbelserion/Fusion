package com.ryderbelserion.fusion.chat;

import com.ryderbelserion.fusion.chat.renderers.ChatRender;
import com.ryderbelserion.fusion.paper.FusionPaper;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class ChatListener implements Listener {

    private final FusionPaper fusion;

    public ChatListener(@NotNull final FusionPaper fusion) {
        this.fusion = fusion;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(AsyncChatEvent event) {
        event.renderer(new ChatRender(this.fusion, event.getPlayer(), "%luckperms_prefix% {player} <gold>-> <reset>{message}", event.signedMessage()));
    }
}