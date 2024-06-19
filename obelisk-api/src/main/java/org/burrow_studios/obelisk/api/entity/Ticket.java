package org.burrow_studios.obelisk.api.entity;

public interface Ticket {
    int getId();

    long getChannelId();

    String getTitle();

    void setTitle(String title);

    void delete();
}
