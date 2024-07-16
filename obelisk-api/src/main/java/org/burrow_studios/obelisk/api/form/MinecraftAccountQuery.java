package org.burrow_studios.obelisk.api.form;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class MinecraftAccountQuery extends QueryElement<UUID> {
    public static final String IDENTIFIER = "minecraft";

    private final AtomicBoolean verified = new AtomicBoolean();

    public MinecraftAccountQuery(@NotNull String id, @Nullable String title, @Nullable String description, boolean optional) {
        super(id, title, description, optional, null);
        this.verified.set(false);
    }

    public MinecraftAccountQuery(@NotNull String id, @Nullable String title, @Nullable String description, boolean optional, UUID account, boolean verified) {
        super(id, title, description, optional, null, account, account != null && verified);
        this.verified.set(account != null && verified);
    }

    /* - - - - - */

    @Override
    public void setValue(UUID account) {
        this.verified.set(false);
        super.setValue(account);
    }

    public boolean isVerified() {
        return this.verified.get();
    }

    public void setVerified(boolean verified) {
        if (this.getValue() == null) return;

        this.verified.set(verified);
    }

    @Override
    public void clear() {
        super.clear();
        this.verified.set(false);
    }

    @Override
    protected void checks(UUID value) throws IllegalArgumentException {
        if (!this.verified.get())
            throw new IllegalArgumentException("Not verified");
    }
}
