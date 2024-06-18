package org.burrow_studios.obelisk.monolith.http.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.auth.ApplicationData;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.burrow_studios.obelisk.monolith.exceptions.NoSuchEntryException;
import org.burrow_studios.obelisk.monolith.http.Request;
import org.burrow_studios.obelisk.monolith.http.Response;
import org.burrow_studios.obelisk.monolith.http.exceptions.BadRequestException;
import org.burrow_studios.obelisk.monolith.http.exceptions.InternalServerErrorException;
import org.burrow_studios.obelisk.monolith.http.exceptions.NotFoundException;
import org.burrow_studios.obelisk.monolith.http.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.util.Pipe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminHandler {
    private final ObeliskMonolith obelisk;

    public AdminHandler(@NotNull ObeliskMonolith obelisk) {
        this.obelisk = obelisk;
    }

    public @NotNull Response onListApplications(@NotNull Request request) throws RequestHandlerException {
        JsonObject responseBody = new JsonObject();

        try {
            Map<Long, String> applications = this.obelisk.getAuthManager().getDatabase().getApplications();

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

    public @NotNull Response onGetApplication(@NotNull Request request) throws RequestHandlerException {
        Long applicationId = request.parsePathSegment(2, Long::parseLong);

        ApplicationData application;

        try {
            application = this.obelisk.getAuthManager().getDatabase().getApplication(applicationId);
        } catch (NoSuchEntryException e) {
            throw new NotFoundException("Application " + applicationId + " not found");
        } catch (DatabaseException e) {
            throw new InternalServerErrorException();
        }

        JsonObject responseBody = new JsonObject();
        responseBody.addProperty("id", application.getId());
        responseBody.addProperty("name", application.getName());
        responseBody.addProperty("pubKey", application.getPubKeyAsString());

        JsonArray intentArr = new JsonArray();
        application.getIntents().forEach(intent -> intentArr.add(intent.name()));
        responseBody.add("intents", intentArr);

        return new Response.Builder()
                .setBody(responseBody)
                .setStatus(200)
                .build();
    }

    public @NotNull Response onCreateApplication(@NotNull Request request) throws RequestHandlerException {
        JsonObject requestJson = request.requireBodyObject();

        String name = Pipe.of(requestJson.get("name"), BadRequestException::new)
                .nonNull("Missing \"name\" attribute")
                .map(JsonElement::getAsString, "Malformed \"name\" attribute")
                .get();


        List<String> intents = new ArrayList<>();
        JsonElement nullableIntents = requestJson.get("intents");
        if (nullableIntents != null) {
            if (!(nullableIntents instanceof JsonArray intentArr))
                throw new BadRequestException("Malformed \"intents\" attributes");

            for (JsonElement intent : intentArr) {
                if (intent instanceof JsonPrimitive p)
                    throw new BadRequestException("Malformed intent element");


            }
        }


        this.obelisk.getAuthManager().getDatabase().createApplication(name, intents);
    }
}
