package org.burrow_studios.obelisk.core.event.gateway;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.event.GatewayEvent;
import org.burrow_studios.obelisk.api.event.entity.EntityCreateEvent;
import org.burrow_studios.obelisk.api.event.entity.EntityDeleteEvent;
import org.burrow_studios.obelisk.api.event.entity.EntityEvent;
import org.burrow_studios.obelisk.api.event.entity.EntityUpdateEvent;
import org.burrow_studios.obelisk.core.entities.impl.TurtleImpl;
import org.burrow_studios.obelisk.core.util.JsonUtil;
import org.jetbrains.annotations.NotNull;

/** Responsible for de-serializing {@link GatewayEvent GatewayEvents} */
public class EventSerializer {
    private EventSerializer() { }

    public static @NotNull JsonObject toJson(@NotNull GatewayEvent event) {
        final Class<? extends GatewayEvent> type = event.getClass();
        final int opcode = GatewayOpcodes.get(type);

        JsonObject content = new JsonObject();

        if (event instanceof EntityEvent<?> eEvent) {
            final TurtleImpl entity = (TurtleImpl) eEvent.getEntity();

            if (eEvent instanceof EntityUpdateEvent<?,?> euEvent) {
                content.addProperty("id", entity.getId());
                JsonUtil.add(content, "old", euEvent.getOldValue());
                JsonUtil.add(content, "new", euEvent.getNewValue());
            } else if (eEvent instanceof EntityCreateEvent<?>) {
                content = entity.toJson();
            } else if (eEvent instanceof EntityDeleteEvent<?>) {
                content.addProperty("id", entity.getId());
            } else {
                throw new RuntimeException("Not implemented (opcode " + opcode + ")");
            }
        } else {
            throw new RuntimeException("Not implemented (opcode " + opcode + ")");
        }

        final JsonObject json = new JsonObject();

        json.addProperty("id", event.getId());
        json.addProperty("op", opcode);
        json.add("c", content);

        return json;
    }

    // event serialization is handled implicitly; Received events are processed and then thrown as new instances
}
