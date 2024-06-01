package org.burrow_studios.obelisk.monolith.http.handlers;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.burrow_studios.obelisk.monolith.http.Request;
import org.burrow_studios.obelisk.monolith.http.Response;
import org.burrow_studios.obelisk.monolith.http.exceptions.InternalServerErrorException;
import org.burrow_studios.obelisk.monolith.http.exceptions.RequestHandlerException;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class AdminHandler {
    private final ObeliskMonolith obelisk;

    public AdminHandler(@NotNull ObeliskMonolith obelisk) {
        this.obelisk = obelisk;
    }

    public @NotNull Response onListApplications(@NotNull Request request) throws RequestHandlerException {
        JsonObject responseBody = new JsonObject();

        try {
            Map<Long, String> applications = obelisk.getAuthManager().getDatabase().getApplications();

            applications.forEach((id, name) -> {
                responseBody.addProperty(String.valueOf(id), name);
            });
        } catch (DatabaseException e) {
            throw new InternalServerErrorException();
        }

        return new Response.Builder()
                .setBody(responseBody)
                .setStatus(200)
                .build();
    }
}
