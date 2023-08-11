package com.minefh.authenticatorbungeecord.cache;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UnauthorizedCache {

    private final List<String> unauthorized;

    public UnauthorizedCache() {
        unauthorized = Collections.synchronizedList(new ArrayList<>());
    }

    public void addPlayerByString(String playerName) {
        unauthorized.add(playerName);
    }

    public void removePlayerByString(String playerName) {
        if(!unauthorized.contains(playerName)) {
            return;
        }
        unauthorized.remove(playerName);
    }
    public void addPlayer(ProxiedPlayer player) {
        unauthorized.add(player.getName());
    }

    public void removePlayer(ProxiedPlayer player) {
        if(!unauthorized.contains(player.getName())) {
            return;
        }
        unauthorized.remove(player.getName());
    }

    public boolean hasPlayer(ProxiedPlayer player) {
        return unauthorized.contains(player.getName());
    }

    public List<String> getUnauthorizedPlayers() {
        return Collections.unmodifiableList(unauthorized);
    }

    public static UnauthorizedCache getInstance() {
        return InstanceHelper.INSTANCE;
    }

    private static class InstanceHelper {
        private static final UnauthorizedCache INSTANCE = new UnauthorizedCache();
    }
}
