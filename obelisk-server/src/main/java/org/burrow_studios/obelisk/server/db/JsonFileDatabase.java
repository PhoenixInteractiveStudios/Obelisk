package org.burrow_studios.obelisk.server.db;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.util.function.ExceptionalConsumer;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

public class JsonFileDatabase {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    private final File dir;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public JsonFileDatabase(@NotNull File dir) throws NotDirectoryException {
        this.dir = dir;

        if (dir.exists())
            if (dir.isDirectory())
                throw new NotDirectoryException(dir.getPath());
        dir.mkdirs();
    }

    public @NotNull Set<Long> getIds() throws DatabaseException {
        return Arrays.stream(
                Optional.ofNullable(dir.listFiles())
                        .orElseThrow(NoSuchEntryException::new)
                )
                .map(File::getName)
                .filter(s -> s.endsWith(".json"))
                .map(s -> s.substring(0, s.length() - ".json".length()))
                .map(s1 -> {
                    try {
                        return Long.parseLong(s1);
                    } catch (NumberFormatException ignored) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableSet());
    }

    public @NotNull JsonObject get(long id) throws DatabaseException {
        File file = getFile(id);

        if (!file.exists())
            throw new NoSuchEntryException();

        try {
            String content = Files.readString(file.toPath());
            return GSON.fromJson(content, JsonObject.class);
        } catch (IOException e) {
            throw new DatabaseException(e);
        }
    }

    public void create(long id, @NotNull JsonObject json) throws DatabaseException {
        File file = getFile(id);

        if (file.exists()) return;

        String content = GSON.toJson(json);

        try {
            Files.writeString(file.toPath(), content, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new DatabaseException(e);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void delete(long id) throws DatabaseException {
        File oldFile = getFile(id);
        File newFile = new File(dir, id + ".json.old");

        if (!oldFile.exists()) return;

        oldFile.renameTo(newFile);
    }

    public void patch(long id, @NotNull ExceptionalConsumer<JsonObject, Exception> patcher) {
        File file = getFile(id);
        JsonObject json = get(id);

        try {
            patcher.accept(json);

            String content = GSON.toJson(json);

            Files.writeString(file.toPath(), content, StandardOpenOption.WRITE);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    private @NotNull File getFile(long id) {
        return new File(dir, id + ".json");
    }
}
