package org.burrow_studios.obelisk.util.turtle;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public final class TurtleGenerator {
    public static final long TIMESTAMP_BITS = 42;
    public static final long   SERVICE_BITS = 14;
    public static final long INCREMENT_BITS = 8;

    private static final long TIMESTAMP_SHIFT = Long.SIZE - TIMESTAMP_BITS;
    private static final long   SERVICE_SHIFT = TIMESTAMP_SHIFT - SERVICE_BITS;

    private static final long INCREMENT_MASK = Long.MAX_VALUE >> (Long.SIZE - INCREMENT_BITS - 1);
    private static final long   SERVICE_MASK = Long.MAX_VALUE >> (Long.SIZE - SERVICE_BITS - 1) << INCREMENT_BITS;

    public static final long EPOCH = Instant.parse("2023-01-01T00:00:00Z").toEpochMilli();

    public final long service;

    TurtleGenerator(int service) {
        this.service = (((long) service) << SERVICE_SHIFT) & SERVICE_MASK;
    }

    public static @NotNull TurtleGenerator get(@NotNull String service) {
        return TurtleGeneratorProvider.getInstance().newGenerator(service);
    }

    private final Object lock = new Object();
    private long latestMilli = System.currentTimeMillis();
    private int  increment   = 0;

    public long newId() {
        long millis = System.currentTimeMillis();

        final long inc;
        synchronized (lock) {
            if (millis != latestMilli) {
                increment = 0;
                latestMilli = millis;
            } else if (increment >= INCREMENT_MASK) {
                // noinspection StatementWithEmptyBody
                while (System.currentTimeMillis() == millis) { }
                millis = System.currentTimeMillis();

                increment = 0;
                latestMilli = millis;
            }

            inc = increment++ & INCREMENT_MASK;
        }

        final long time = (System.currentTimeMillis() - EPOCH) << TIMESTAMP_SHIFT;

        return time | service | inc;
    }

    static long newId(long unixMillis, int service, int increment) {
        final long time = (unixMillis - EPOCH) << TIMESTAMP_SHIFT;
        final long serv = (long) service << SERVICE_SHIFT;
        final long inc  = increment & INCREMENT_MASK;

        return time | serv | inc;
    }

    public static long getTime(long turtle) {
        return (turtle >> TIMESTAMP_SHIFT) + EPOCH;
    }

    public static int getService(long turtle) {
        return (int) ((turtle & SERVICE_MASK) >> SERVICE_SHIFT);
    }

    public static int getIncrement(long turtle) {
        return (int) (turtle & INCREMENT_MASK);
    }
}
