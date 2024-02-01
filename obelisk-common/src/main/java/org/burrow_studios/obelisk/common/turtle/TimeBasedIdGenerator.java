package org.burrow_studios.obelisk.common.turtle;

import org.jetbrains.annotations.NotNull;

public final class TimeBasedIdGenerator {
    public static final long TIMESTAMP_BITS = 42;
    public static final long INCREMENT_BITS = 22;

    private static final long TIMESTAMP_SHIFT = Long.SIZE - TIMESTAMP_BITS;
    private static final long INCREMENT_MASK  = Long.MAX_VALUE >> (Long.SIZE - INCREMENT_BITS - 1);

    private static final long EPOCH = TurtleGenerator.EPOCH;

    private TimeBasedIdGenerator() { }

    public static @NotNull TimeBasedIdGenerator get() {
        return new TimeBasedIdGenerator();
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

        return time | inc;
    }

    public static long getTime(long id) {
        return (id >> TIMESTAMP_SHIFT) + EPOCH;
    }

    public static int getIncrement(long id) {
        return (int) (id & INCREMENT_MASK);
    }
}
