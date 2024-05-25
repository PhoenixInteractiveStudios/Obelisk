package org.burrow_studios.obelisk.client.config;

public class GatewayConfig {
    private String host;
    private Integer port;

    public GatewayConfig() { }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isEmpty() {
        return this.host == null || this.port == null;
    }
}
