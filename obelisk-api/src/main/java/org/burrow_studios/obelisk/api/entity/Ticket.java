package org.burrow_studios.obelisk.api.entity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Ticket {
    int getId();
    long getChannelId();
    @NotNull List<? extends User> getUsers();

    void delete();
}
