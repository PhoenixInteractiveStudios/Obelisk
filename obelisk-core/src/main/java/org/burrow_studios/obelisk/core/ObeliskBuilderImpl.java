package org.burrow_studios.obelisk.core;

import org.burrow_studios.obelisk.api.ObeliskBuilder;
import org.burrow_studios.obelisk.core.event.EventHandlerImpl;
import org.burrow_studios.obelisk.core.net.NetworkHandler;
import org.burrow_studios.obelisk.core.source.DataProvider;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

@SuppressWarnings("unused")
public class ObeliskBuilderImpl implements ObeliskBuilder {
    private String token;
    private Function<ObeliskImpl, EventHandlerImpl> eventHandlerSupplier;
    private Function<ObeliskImpl, DataProvider> dataProviderSupplier;

    /** @see ObeliskBuilder#create() */
    @SuppressWarnings("unused")
    public ObeliskBuilderImpl() { }

    @Override
    public @NotNull ObeliskBuilderImpl setToken(String token) {
        this.token = token;
        return this;
    }

    public @NotNull ObeliskBuilderImpl setEventHandler(@NotNull Function<ObeliskImpl, EventHandlerImpl> supplier) {
        this.eventHandlerSupplier = supplier;
        return this;
    }

    public @NotNull ObeliskBuilderImpl setDataProvider(@NotNull Function<ObeliskImpl, DataProvider> supplier) {
        this.dataProviderSupplier = supplier;
        return this;
    }

    @Override
    public @NotNull ObeliskImpl build() throws IllegalArgumentException {
        if (token == null)
            throw new IllegalArgumentException("Token may not be null");

        if (eventHandlerSupplier == null)
            eventHandlerSupplier = EventHandlerImpl::new;

        if (dataProviderSupplier == null)
            dataProviderSupplier = NetworkHandler::new;

        return new ObeliskImpl(
                token,
                eventHandlerSupplier,
                dataProviderSupplier
        );
    }
}
