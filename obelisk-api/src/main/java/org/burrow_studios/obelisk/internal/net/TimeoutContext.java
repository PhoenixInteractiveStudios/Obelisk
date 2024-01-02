package org.burrow_studios.obelisk.internal.net;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public final class TimeoutContext {
    public static final TimeoutContext DEFAULT = timeout(4, TimeUnit.SECONDS);

    private final long time;
    private final boolean fixed;

    private TimeoutContext(long time, boolean fixed) {
        this.time = time;
        this.fixed = fixed;
    }

    public boolean isElapsed() {
        if (!fixed) return true;
        return System.currentTimeMillis() > time;
    }

    public long asDeadline() {
        return fixed ? time : (System.currentTimeMillis() + time);
    }

    public long asTimeout() {
        return fixed ? (time - System.currentTimeMillis()) : time;
    }

    public long asTimeout(@NotNull TimeUnit unit) {
        return unit.convert(this.asTimeout(), TimeUnit.MILLISECONDS);
    }

    public @NotNull TimeoutContext toDeadline() {
        if (fixed) return this;
        return new TimeoutContext(System.currentTimeMillis() + time, true);
    }

    public @NotNull Duration asDuration() {
        return Duration.of(asTimeout(), ChronoUnit.MILLIS);
    }

    /* - - - */

    /** Creates a new context that times out {@code time} amount of {@code unit} after execution. */
    public static @NotNull TimeoutContext timeout(long time, @NotNull TimeUnit unit) {
        return new TimeoutContext(unit.toMillis(time), false);
    }

    /** Creates a new context that times out {@code time} milliseconds after execution. */
    public static @NotNull TimeoutContext timeout(long millis) {
        return new TimeoutContext(millis, false);
    }

    /**
     * Creates a new context that times out {@code time} amount of {@code unit}, regardless of when the execution
     * occurs. The deadline is computed when this method is called.
     * <p> Calling this method is equivalent to:
     * <pre>{@code  TimeoutContext.timeout(System.currentTimeMillis() + unit.toMillis(time))}</pre>
     */
    public static @NotNull TimeoutContext deadline(long time, @NotNull TimeUnit unit) {
        return new TimeoutContext(System.currentTimeMillis() + unit.toMillis(time), true);
    }

    /** Creates a new context with a deadline at an {@code instant} (unix millis). */
    public static @NotNull TimeoutContext deadline(long instant) {
        return new TimeoutContext(instant, true);
    }

    /** Creates a new context with a deadline at an {@code instant}. */
    public static @NotNull TimeoutContext deadline(@NotNull Instant instant) {
        return new TimeoutContext(instant.toEpochMilli(), true);
    }
}
