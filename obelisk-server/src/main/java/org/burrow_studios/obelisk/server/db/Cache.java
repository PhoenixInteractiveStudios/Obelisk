package org.burrow_studios.obelisk.server.db;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

public class Cache<T> {
    private final Object lock = new Object();
    private HashMap<Long, T> map = new HashMap<>();

    public @NotNull Optional<T> get(long id) {
        synchronized (lock) {
            return Optional.ofNullable(map.get(id));
        }
    }

    public void put(long id, T content) {
        synchronized (lock) {
            map.put(id, content);
        }
    }

    public void add(long id) {
        synchronized (lock) {
            map.putIfAbsent(id, null);
        }
    }

    public void remove(long id) {
        synchronized (lock) {
            map.remove(id);
        }
    }

    public Set<Long> getIds() {
        synchronized (lock) {
            return Set.copyOf(map.keySet());
        }
    }
}
