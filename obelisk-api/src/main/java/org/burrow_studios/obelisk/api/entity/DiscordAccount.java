package org.burrow_studios.obelisk.api.entity;

public interface DiscordAccount {
    long getSnowflake();

    String getName();

    void setName(String name);

    void delete();
}
