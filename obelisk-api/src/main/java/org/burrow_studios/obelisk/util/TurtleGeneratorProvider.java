package org.burrow_studios.obelisk.util;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

class TurtleGeneratorProvider {
    private static final TurtleGeneratorProvider INSTANCE = new TurtleGeneratorProvider();

    public static @NotNull TurtleGeneratorProvider getInstance() {
        return INSTANCE;
    }

    /* - - - */

    private final ConcurrentHashMap<Integer, TurtleGenerator> generators;

    TurtleGeneratorProvider() {
        this.generators = new ConcurrentHashMap<>();
    }

    public synchronized @NotNull TurtleGenerator newGenerator(@NotNull String str) {
        int service = Objects.hashCode(str);

        TurtleGenerator gen = this.generators.get(service);

        if (gen == null) {
            gen = new TurtleGenerator(service);
            this.generators.put(service, gen);
        }

        return gen;
    }
}
