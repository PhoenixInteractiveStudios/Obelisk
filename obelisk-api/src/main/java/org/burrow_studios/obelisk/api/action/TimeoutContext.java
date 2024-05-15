package org.burrow_studios.obelisk.api.action;

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

    /**
     * Returns true if this TimeoutContext is fixed (not relative) and the timestamp has already been surpassed. If this
     * TimeoutContext is relative, this method will always return {@code true}.
     */
    public boolean isElapsed() {
        if (!fixed) return true;
        return System.currentTimeMillis() > time;
    }

    /**
     * Returns this TimeoutContext as a fixed timestamp. If this TimeoutContext is relative, the returned timestamp will
     * be the current time plus the relative timeout.
     */
    public long asDeadline() {
        return fixed ? time : (System.currentTimeMillis() + time);
    }

    /**
     * Returns this TimeoutContext as a relative timeout. If this TimeoutContext is fixed, the returned timestamp will
     * be the relative time to that fixed timestamp.
     */
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

    public @NotNull Instant asInstant() {
        return Instant.ofEpochMilli(this.asDeadline());
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
