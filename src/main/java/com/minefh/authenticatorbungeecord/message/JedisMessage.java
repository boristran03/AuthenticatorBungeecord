package com.minefh.authenticatorbungeecord.message;

import com.google.gson.Gson;
import com.minefh.authenticatorbungeecord.AuthenticatorBungeecord;
import com.minefh.authenticatorbungeecord.cache.UnauthorizedCache;
import com.minefh.authenticatorbungeecord.config.Config;
import com.minefh.authenticatorbungeecord.config.ConfigParser;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

public class JedisMessage {

    private final String CHANNEL_ID = "ls_authenticated";
    private final AuthenticatorBungeecord plugin;
    private final Config config;
    private JedisPool jedisPool;
    private final Gson gson;
    private Thread thread;
    public JedisMessage(AuthenticatorBungeecord plugin) {


        this.plugin = plugin;
        this.gson = new Gson();

        this.config = ConfigParser.getInstance().getConfig();
    }

    public void init() {

        String host = config.getHost();
        String password = config.getPassword();
        int port = config.getPort();
        boolean useSSL = config.isUseSSL();


        this.jedisPool = password.isEmpty() ? new JedisPool(new JedisPoolConfig(), host, port, 0, useSSL)
                : new JedisPool(new JedisPoolConfig(), host, port, 0, password, useSSL);


        this.thread = new Thread(getSubscriber(), "redis_subscriber");
        thread.start();

        plugin.getLogger().info("Connect to redis successfully! Ready to listen!");
    }

    private Runnable getSubscriber() {
        return () -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String encodedMessage) {
                        if (!channel.equals(CHANNEL_ID)) {
                            return;
                        }
                        LoginMessage message;
                        try {
                            message = gson.fromJson(encodedMessage, LoginMessage.class);
                        } catch (Exception e) {
                            plugin.getLogger().warning("Failed to decode message from Redis: " + e.getMessage());
                            return;
                        }

                        UnauthorizedCache cache = UnauthorizedCache.getInstance();
                        cache.removePlayerByString(message.getPlayerName());
                        plugin.getLogger().info(message.getPlayerName() + " has logged in successfully!");
                    }
                }, CHANNEL_ID);
            }
        };
    }

    public void close() {
        if(thread != null && thread.isAlive()) {
            thread.interrupt();
        }
        if (jedisPool != null) {
            jedisPool.close();
        }
    }
}
