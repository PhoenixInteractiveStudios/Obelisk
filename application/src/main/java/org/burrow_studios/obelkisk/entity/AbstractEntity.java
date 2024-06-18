package org.burrow_studios.obelkisk.entity;

abstract class AbstractEntity {
    protected final long id;

    protected AbstractEntity(long id) {
        this.id = id;
    }

    public final long getId() {
        return this.id;
    }

    public abstract void delete();
}
