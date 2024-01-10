package org.burrow_studios.obelisk.core.action.entity.user;

import org.burrow_studios.obelisk.api.action.entity.user.UserModifier;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.core.action.ModifierImpl;
import org.burrow_studios.obelisk.core.data.UserData;
import org.burrow_studios.obelisk.core.entities.UserImpl;
import org.burrow_studios.obelisk.core.net.http.Route;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class UserModifierImpl extends ModifierImpl<User, UserImpl, UserData> implements UserModifier {
    public UserModifierImpl(@NotNull UserImpl user) {
        super(
                user,
                Route.User.EDIT.builder()
                        .withArg(user.getId())
                        .compile(),
                new UserData(user.getId()),
                UserData::new
        );
    }

    @Override
    public @NotNull UserModifierImpl setName(@NotNull String name) {
        this.data.setName(name);
        return this;
    }

    @Override
    public @NotNull UserModifierImpl addDiscordIds(long... ids) {
        this.data.addDiscordIds(ids);
        return this;
    }

    @Override
    public @NotNull UserModifierImpl removeDiscordIds(long... ids) {
        this.data.removeDiscordIds(ids);
        return this;
    }

    @Override
    public @NotNull UserModifierImpl addMinecraftIds(@NotNull UUID... ids) {
        this.data.addMinecraftIds(ids);
        return this;
    }

    @Override
    public @NotNull UserModifierImpl removeMinecraftIds(@NotNull UUID... ids) {
        this.data.removeMinecraftIds(ids);
        return this;
    }
}
