package org.burrow_studios.obelisk.commons.rpc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.commons.function.ExceptionalFunction;
import org.burrow_studios.obelisk.commons.rpc.exceptions.BadRequestException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class RequestBodyHelper {
    private final RPCRequest request;

    RequestBodyHelper(RPCRequest request) {
        this.request = request;
    }

    public @Nullable JsonElement requireBody() throws BadRequestException {
        if (request.getBody() == null)
            throw new BadRequestException("Missing request body");
        return request.getBody();
    }

    public <T extends JsonElement> @NotNull T requireBody(@NotNull Class<T> type) throws BadRequestException {
        JsonElement body = this.requireBody();
        if (!type.isInstance(body))
            throw new BadRequestException("Body is not of expected type '" + type.getSimpleName() + "'");
        return type.cast(body);
    }

    public @NotNull JsonElement requireElement(@NotNull String name) throws BadRequestException {
        JsonObject json = this.requireBody(JsonObject.class);
        JsonElement element = json.get(name);
        if (element == null)
            throw new BadRequestException("Malformed body: Missing element '" + name + "'");
        return element;
    }

    public <T> @NotNull T requireElement(@NotNull String name, @NotNull ExceptionalFunction<JsonElement, T, ? extends Exception> mapper) throws BadRequestException {
        try {
            return mapper.apply(this.requireElement(name));
        } catch (Exception ignored) {
            throw new BadRequestException("Malformed body: Malformed element '" + name + "'");
        }
    }

    public <T> @NotNull Optional<T> optionalElement(@NotNull String name, @NotNull ExceptionalFunction<JsonElement, T, ? extends Exception> mapper) throws BadRequestException {
        JsonObject json = this.requireBody(JsonObject.class);
        JsonElement element = json.get(name);

        if (element == null)
            return Optional.empty();

        try {
            return Optional.of(mapper.apply(element));
        } catch (Exception e) {
            throw new BadRequestException("Malformed body: Malformed element '" + name + "'");
        }
    }

    public @NotNull String requireElementAsString(@NotNull String name) throws BadRequestException {
        JsonElement element = this.requireElement(name);
        if (!(element instanceof JsonPrimitive))
            throw new BadRequestException("Malformed request body: Malformed element '" + name + "'");
        return element.getAsString();
    }

    public @NotNull Optional<String> optionalElementAsString(@NotNull String name) throws BadRequestException {
        JsonObject json = this.requireBody(JsonObject.class);
        JsonElement element = json.get(name);

        if (element == null)
            return Optional.empty();

        if (!(element instanceof JsonPrimitive primitive))
            throw new BadRequestException("Malformed request body: Malformed element '" + name + "'");

        return Optional.of(primitive.getAsString());
    }

    public int requireElementAsInt(@NotNull String name) throws BadRequestException {
        JsonElement element = this.requireElement(name);
        if (!(element instanceof JsonPrimitive primitive) || !primitive.isNumber())
            throw new BadRequestException("Malformed request body: Malformed element '" + name + "'");
        return primitive.getAsInt();
    }

    public @NotNull Optional<Integer> optionalElementAsInt(@NotNull String name) throws BadRequestException {
        JsonObject json = this.requireBody(JsonObject.class);
        JsonElement element = json.get(name);

        if (element == null)
            return Optional.empty();

        if (!(element instanceof JsonPrimitive primitive) || !primitive.isNumber())
            throw new BadRequestException("Malformed request body: Malformed element '" + name + "'");

        return Optional.of(primitive.getAsInt());
    }

    public long requireElementAsLong(@NotNull String name) throws BadRequestException {
        JsonElement element = this.requireElement(name);
        if (!(element instanceof JsonPrimitive primitive) || !primitive.isNumber())
            throw new BadRequestException("Malformed request body: Malformed element '" + name + "'");
        return primitive.getAsLong();
    }

    public @NotNull Optional<Long> optionalElementAsLong(@NotNull String name) throws BadRequestException {
        JsonObject json = this.requireBody(JsonObject.class);
        JsonElement element = json.get(name);

        if (element == null)
            return Optional.empty();

        if (!(element instanceof JsonPrimitive primitive) || !primitive.isNumber())
            throw new BadRequestException("Malformed request body: Malformed element '" + name + "'");

        return Optional.of(primitive.getAsLong());
    }
}
