package org.burrow_studios.obelisk.persistence;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Optional;

public class PersistentConfig {
    private static final Logger LOG = LoggerFactory.getLogger(PersistentConfig.class);

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private final File file;

    public PersistentConfig(File file) {
        this.file = file;
    }

    private JsonObject read() {
        try {
            return GSON.fromJson(new FileReader(file), JsonObject.class);
        } catch (FileNotFoundException e) {
            LOG.debug("persistence.json does not exist yet");
            return null;
        } catch (JsonIOException | JsonSyntaxException e) {
            LOG.warn("Failed to read file or parse JSON", e);

            // can't really be handled here without risking unpredictable IOExceptions everywhere else
            return null;
        }
    }

    private void write(@NotNull String key, @NotNull JsonElement element) {
        JsonObject json = this.read();

        if (json == null)
            json = new JsonObject();

        json.add(key, element);

        try (FileWriter writer = new FileWriter(this.file, false)) {
            GSON.toJson(json, writer);
        } catch (IOException e) {
            LOG.warn("Failed to write file");
        }
    }

    public Optional<Long> getLong(@NotNull String key) {
        return Optional.ofNullable(this.read())
                .map(json -> json.get(key))
                .filter(JsonElement::isJsonPrimitive)
                .map(JsonElement::getAsJsonPrimitive)
                .filter(JsonPrimitive::isNumber)
                .map(JsonPrimitive::getAsLong);
    }

    public void setLong(@NotNull String key, long val) {
        this.write(key, new JsonPrimitive(val));
    }
}
