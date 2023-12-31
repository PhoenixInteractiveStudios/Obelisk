package org.burrow_studios.obelisk.internal.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.User;
import org.jetbrains.annotations.NotNull;

public final class GroupData extends Data<Group> {
    public GroupData() {
        super();
    }

    public GroupData(long id) {
        super(id);
    }

    public void setName(@NotNull String name) {
        this.set("name", new JsonPrimitive(name));
    }

    public void setPosition(int position) {
        this.set("position", new JsonPrimitive(position));
    }

    public void addMembers(@NotNull User... users) {
        JsonArray arr = new JsonArray();
        for (User user : users)
            arr.add(user.getId());
        this.addToArray("members", arr);
    }

    public void removeMembers(@NotNull User... users) {
        JsonArray arr = new JsonArray();
        for (User user : users)
            arr.add(user.getId());
        this.removeFromArray("members", arr);
    }
}
