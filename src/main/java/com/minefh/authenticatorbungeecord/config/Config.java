package com.minefh.authenticatorbungeecord.config;

public class Config {

    private String host = "localhost";
    private String password = "";
    private int port = 6379;
    private boolean useSSL = true;

    public String getHost() {
        return host;
    }

    public String getPassword() {
        return password;
    }

    public int getPort() {
        return port;
    }

    public boolean isUseSSL() {
        return useSSL;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
    }
}
