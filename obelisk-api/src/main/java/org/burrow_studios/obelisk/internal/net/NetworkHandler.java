package org.burrow_studios.obelisk.internal.net;

import org.burrow_studios.obelisk.common.TimeBasedIdGenerator;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.burrow_studios.obelisk.internal.action.ActionImpl;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class NetworkHandler {
    private final ObeliskImpl api;
    private final TimeBasedIdGenerator requestIdGenerator = TimeBasedIdGenerator.get();
    private final ConcurrentHashMap<Long, Request> pendingRequests = new ConcurrentHashMap<>();

    public NetworkHandler(@NotNull ObeliskImpl api) {
        this.api = api;
    }

    public @NotNull Request submitRequest(@NotNull ActionImpl<?> action) {
        final long id = this.requestIdGenerator.newId();

        // this is a default for now | TODO: make this controllable on the API level
        final long deadline = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(8);

        final Request request = new Request(
                this,
                id,
                action.getRoute(),
                action.getContent(),
                deadline
        );

        this.pendingRequests.put(id, request);
        return request;
    }

    public void submitResponse(@NotNull Response response) {
        final Request request = this.pendingRequests.remove(response.getId());

        // TODO: log
        if (request == null) return;

        request.getFuture().complete(response);
    }
}
