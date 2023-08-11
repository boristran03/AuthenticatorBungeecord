package com.minefh.authenticatorbungeecord;

import com.minefh.authenticatorbungeecord.config.ConfigParser;
import com.minefh.authenticatorbungeecord.listeners.PlayerListener;
import com.minefh.authenticatorbungeecord.message.JedisMessage;
import net.md_5.bungee.api.plugin.Plugin;

public final class AuthenticatorBungeecord extends Plugin {

    private static AuthenticatorBungeecord __instance;
    private JedisMessage jedisMessage;
    @Override
    public void onEnable() {
        __instance = this;

        new ConfigParser(this).init();

        this.jedisMessage = new JedisMessage(this);
        jedisMessage.init();

        getProxy().getPluginManager().registerListener(this, new PlayerListener());
    }

    @Override
    public void onDisable() {
        jedisMessage.close();
    }

    public static AuthenticatorBungeecord getInstance() {
        return __instance;
    }
}
