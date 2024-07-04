package org.burrow_studios.obelkisk.core.db.file;

import com.google.gson.*;
import org.burrow_studios.obelisk.util.concurrent.locks.CloseableLock;
import org.burrow_studios.obelisk.util.concurrent.locks.ReadWriteCloseableLock;
import org.burrow_studios.obelkisk.core.Obelisk;
import org.burrow_studios.obelkisk.core.db.interfaces.FormDB;
import org.burrow_studios.obelkisk.core.entity.Form;
import org.burrow_studios.obelkisk.core.entity.User;
import org.burrow_studios.obelkisk.core.exceptions.DatabaseException;
import org.burrow_studios.obelkisk.core.exceptions.NoSuchEntryException;
import org.burrow_studios.obelkisk.core.form.FormElement;
import org.burrow_studios.obelkisk.core.form.FormParser;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;

public class FSFormDB implements FormDB {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    private final Obelisk obelisk;
    private final File directory;

    private final Map<Integer, Form> cache = Collections.synchronizedMap(new WeakHashMap<>());
    private final ReadWriteCloseableLock lock = new ReadWriteCloseableLock();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public FSFormDB(@NotNull Obelisk obelisk, @NotNull File directory) {
        this.obelisk = obelisk;
        this.directory = directory;

        directory.mkdir();
    }

    @Override
    public @NotNull Form createForm(@NotNull User author, @NotNull String template, @NotNull List<FormElement> elements) throws DatabaseException {
        try (CloseableLock ignored = this.lock.write()) {
            final int id = this.listForms().stream().mapToInt(v -> v).max().orElse(0);
            Form form = new Form(id, this, author, template, elements);
            this.updateForm(form);
            return form;
        }
    }

    @Override
    public @NotNull List<Integer> listForms() throws DatabaseException {
        String[] filenames;

        try (CloseableLock ignored = this.lock.read()) {
            filenames = this.directory.list();
        }

        if (filenames == null)
            filenames = new String[]{};

        return Arrays.stream(filenames)
                .filter(s -> s.matches("^\\d+\\.json$"))
                .map(s -> s.substring(0, s.length() - ".json".length()))
                .map(Integer::parseInt)
                .toList();
    }

    @Override
    public @NotNull Form getForm(int id) throws DatabaseException {
        Form cachedForm = this.cache.get(id);
        if (cachedForm != null)
            return cachedForm;

        File file = new File(this.directory, id + ".json");

        try (
                CloseableLock ignored = this.lock.read();
                FileReader reader = new FileReader(file)
        ) {
            JsonObject json = GSON.fromJson(reader, JsonObject.class);

            long authorId = json.get("author").getAsLong();
            User author = this.obelisk.getUserDB().getUser(authorId);

            String template = json.get("template").getAsString();

            JsonArray elementJson = json.getAsJsonArray("elements");
            List<FormElement> elements = FormParser.fromJson(elementJson);

            Form form = new Form(id, this, author, template, elements);
            this.cache.put(id, form);
            return form;
        } catch (FileNotFoundException e) {
            throw new NoSuchEntryException();
        } catch (IOException | JsonIOException | JsonSyntaxException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void updateForm(@NotNull Form form) throws DatabaseException {
        JsonObject json = new JsonObject();

        json.addProperty("author", form.getAuthor().getId());
        json.addProperty("template", form.getTemplate());

        JsonArray elements = FormParser.toJson(form.getElements());
        json.add("elements", elements);

        File file = new File(this.directory, form.getId() + ".json");

        try (
                CloseableLock ignored = this.lock.write();
                FileWriter writer = new FileWriter(file, false)
        ) {
            GSON.toJson(json, writer);
        } catch (IOException e) {
            throw new DatabaseException(e);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void deleteForm(int id) throws DatabaseException {
        try (CloseableLock ignored = this.lock.write()) {
            File file = new File(this.directory, id + ".json");
            file.delete();
        }

        // just to be sure
        this.cache.remove(id);
    }
}
