package org.burrow_studios.obelisk.util.concurrent;

import org.burrow_studios.obelisk.util.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class EnumLatch<E extends Enum<E>> {
    private final ReadWriteLock lock;
    private final Map<Thread, Pair<E, Condition>> conditions;
    private volatile E value;

    public EnumLatch(@NotNull E initialValue) {
        this.lock       = new ReentrantReadWriteLock();
        this.conditions = new HashMap<>();
        this.value      = initialValue;
    }

    public void await(@NotNull E value) throws InterruptedException {
        lock.readLock().lock();

        try {
            // immediately return if the condition is already met
            if (this.value.equals(value))
                return;

            final Thread currentThread = Thread.currentThread();
            final Condition  condition = lock.readLock().newCondition();

            // register condition
            conditions.put(currentThread, new Pair<>(value, condition));

            // block thread
            condition.await();
        } finally {
            conditions.remove(Thread.currentThread());
            lock.readLock().unlock();
        }
    }

    public boolean await(@NotNull E value, long time, @NotNull TimeUnit unit) throws InterruptedException {
        lock.readLock().lock();

        try {
            // immediately return if the condition is already met
            if (this.value.equals(value))
                return false;

            final Thread currentThread = Thread.currentThread();
            final Condition  condition = lock.readLock().newCondition();

            // register condition
            conditions.put(currentThread, new Pair<>(value, condition));

            // block thread
            return condition.await(time, unit);
        } finally {
            conditions.remove(Thread.currentThread());
            lock.readLock().unlock();
        }
    }

    public void set(@NotNull E value) {
        lock.writeLock().lock();

        try {
            this.update(this.value, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean update(@NotNull E from, @NotNull E to) {
        lock.writeLock().lock();

        try {
            if (!this.value.equals(from))
                 return false;

            this.value = to;

            for (Pair<E, Condition> condition : conditions.values()) {
                // check if the condition awaits the new value
                if (!condition.first().equals(value)) continue;

                condition.second().signalAll();
            }
        } finally {
            lock.writeLock().unlock();
        }
        return true;
    }

    public @NotNull E get() {
        lock.readLock().lock();

        try {
            return this.value;
        } finally {
            lock.readLock().unlock();
        }
    }
}
