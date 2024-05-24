package org.burrow_studios.obelisk.core.entities;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.entities.DiscordAccount;
import org.burrow_studios.obelisk.core.AbstractObelisk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractDiscordAccount extends AbstractEntity implements DiscordAccount {
    private final long snowflake;
    private @NotNull String cachedName;
    private @Nullable AbstractUser user;

    protected AbstractDiscordAccount(
            @NotNull AbstractObelisk obelisk,
            long id,
            long snowflake,
            @NotNull String cachedName,
            @Nullable AbstractUser user
    ) {
        super(obelisk, id);
        this.snowflake = snowflake;
        this.cachedName = cachedName;
        this.user = user;
    }

    @Override
    public final @NotNull JsonObject toJson() {
        JsonObject json = this.toMinimalJson();

        // prevent concurrent modifications
        AbstractUser user = this.user;
        if (user == null) {
            json.add("user", JsonNull.INSTANCE);
        } else {
            JsonObject uJson = new JsonObject();
            uJson.addProperty("id", user.getId());
            uJson.addProperty("name", user.getName());
            json.add("user", uJson);
        }

        return json;
    }

    public final @NotNull JsonObject toMinimalJson() {
        JsonObject json = new JsonObject();
        json.addProperty("id", this.id);
        json.addProperty("snowflake", this.snowflake);
        json.addProperty("name", this.cachedName);
        return json;
    }

    @Override
    public final long getSnowflake() {
        return this.snowflake;
    }

    @Override
    public final @NotNull String getCachedName() {
        return this.cachedName;
    }

    public final void setCachedName(@NotNull String cachedName) {
        this.cachedName = cachedName;
    }

    @Override
    public @Nullable AbstractUser getUser() {
        return this.user;
    }

    public void setUser(@Nullable AbstractUser user) {
        this.user = user;
    }
}
