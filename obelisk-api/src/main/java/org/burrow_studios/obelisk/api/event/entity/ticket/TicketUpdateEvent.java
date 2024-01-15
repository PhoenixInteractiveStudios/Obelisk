package org.burrow_studios.obelisk.api.event.entity.ticket;

import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.event.entity.EntityUpdateEvent;
import org.jetbrains.annotations.NotNull;

public abstract sealed class TicketUpdateEvent<T> extends TicketEvent implements EntityUpdateEvent<Ticket, T> permits TicketUpdateStateEvent, TicketUpdateTagsEvent, TicketUpdateTitleEvent, TicketUpdateUsersEvent {
    protected final T oldValue;
    protected final T newValue;

    protected TicketUpdateEvent(long id, @NotNull Ticket entity, T oldValue, T newValue) {
        super(id, entity);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    public final T getOldValue() {
        return this.oldValue;
    }

    @Override
    public final T getNewValue() {
        return this.newValue;
    }
}
