package com.minefh.authenticatorbungeecord.listeners;

import com.minefh.authenticatorbungeecord.cache.UnauthorizedCache;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.Arrays;
import java.util.List;

public class PlayerListener implements Listener {

    //MANY THREADS MAY ACCESS TO THIS LIST
    private final UnauthorizedCache cache;
    private final List<String> commandWhitelist;

    public PlayerListener() {
        this.cache = UnauthorizedCache.getInstance();
        this.commandWhitelist = Arrays.asList("/login", "/register", "/email");
    }
    @EventHandler
    public void onJoin(PostLoginEvent e) {
        ProxiedPlayer player = e.getPlayer();

        cache.addPlayer(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommand(ChatEvent e) {
        if(e.isCancelled() || !e.isCommand()) {
            return;
        }
        if(!(e.getSender() instanceof ProxiedPlayer)) {
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) e.getSender();

        if(!cache.hasPlayer(player)) {
            return;
        }
        String command = e.getMessage().split(" ")[0].toLowerCase();
        if(!commandWhitelist.contains(command)) {
            sendDenyMessage(player);
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(ChatEvent e) {
        if (e.isCancelled() || e.isCommand()) {
            return;
        }
        if(!(e.getSender() instanceof ProxiedPlayer)) {
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) e.getSender();
        if(cache.hasPlayer(player)) {
            sendDenyMessage(player);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onLeave(PlayerDisconnectEvent e) {
        ProxiedPlayer player = e.getPlayer();

        if(cache.hasPlayer(player)) {
            cache.removePlayer(player);
        }
    }

    private void sendDenyMessage(ProxiedPlayer player) {
        player.sendMessage("§cBạn cần đăng nhập để thưc hiện hành động này!");
    }
}
