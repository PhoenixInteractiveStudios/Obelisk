package org.burrow_studios.obelisk.api.entities;

import org.burrow_studios.obelisk.api.Obelisk;
import org.jetbrains.annotations.NotNull;

/**
 * A turtle entity is an entity that can be uniquely identified by its turtle id.
 * @see Turtle#getId()
 */
public interface Turtle {
    /**
     * Provides the unique turtle id of this entity. This id should never change and always only refer to this entity.
     * @return Long representation of the id.
     */
    long getId();

    /**
     * Provides the {@link Obelisk API} instance responsible for this entity. The API handles caching of entities and
     * provides methods to retrieve, modify, create or delete them.
     * @return Responsible TurtleClient
     */
    @NotNull Obelisk getAPI();
}
