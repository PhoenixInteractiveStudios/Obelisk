package org.burrow_studios.obelkisk.core.form;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MinecraftAccountQuery extends QueryElement<UUID> {
    public static final String IDENTIFIER = "minecraft";

    private boolean verified;

    public MinecraftAccountQuery(@NotNull String id, @NotNull String title, boolean optional) {
        super(id, title, optional, null);
        this.verified = false;
    }

    public MinecraftAccountQuery(@NotNull String id, @NotNull String title, boolean optional, UUID account, boolean verified) {
        super(id, title, optional, null, account, account != null && verified);
        this.verified = account != null && verified;
    }

    /* - - - - - */

    @Override
    public void setValue(UUID account) {
        this.verified = false;
        super.setValue(account);
    }

    public boolean isVerified() {
        return this.verified;
    }

    public void setVerified(boolean verified) {
        if (this.getValue() == null) return;

        this.verified = verified;
    }

    @Override
    public void clear() {
        super.clear();
        this.verified = false;
    }

    @Override
    protected void checks(UUID value) throws IllegalArgumentException {
        if (!this.verified)
            throw new IllegalArgumentException("Not verified");
    }
}
