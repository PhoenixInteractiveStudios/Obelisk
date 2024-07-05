package org.burrow_studios.obelkisk.server.db.file;

import com.google.gson.*;
import org.burrow_studios.obelisk.api.entity.Form;
import org.burrow_studios.obelisk.api.entity.User;
import org.burrow_studios.obelisk.api.entity.dao.FormDAO;
import org.burrow_studios.obelisk.api.form.FormElement;
import org.burrow_studios.obelisk.util.concurrent.locks.CloseableLock;
import org.burrow_studios.obelisk.util.concurrent.locks.ReadWriteCloseableLock;
import org.burrow_studios.obelkisk.server.Obelisk;
import org.burrow_studios.obelkisk.server.exceptions.DatabaseException;
import org.burrow_studios.obelkisk.server.exceptions.NoSuchEntryException;
import org.burrow_studios.obelkisk.server.form.FormParser;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;

public class FSFormDB implements FormDAO {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    private final Obelisk obelisk;

    private final File templatesDirectory;
    private final File submissionsDirectory;

    private final Map<Integer, Form> cache = Collections.synchronizedMap(new WeakHashMap<>());
    private final ReadWriteCloseableLock lock = new ReadWriteCloseableLock();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public FSFormDB(@NotNull Obelisk obelisk, @NotNull File directory) {
        this.obelisk = obelisk;

        directory.mkdir();

        this.templatesDirectory = new File(directory, "templates");
        this.templatesDirectory.mkdir();

        this.submissionsDirectory = new File(directory, "submissions");
        this.submissionsDirectory.mkdir();
    }

    @Override
    public @NotNull Form createForm(@NotNull User author, @NotNull String template) throws DatabaseException {
        File file = new File(this.templatesDirectory, template + ".json");

        List<FormElement> elements;

        try (
                CloseableLock ignored = this.lock.read();
                FileReader reader = new FileReader(file)
        ) {
            JsonObject json = GSON.fromJson(reader, JsonObject.class);

            JsonArray elementJson = json.getAsJsonArray("elements");
            elements = FormParser.fromJson(elementJson);
        } catch (FileNotFoundException e) {
            throw new NoSuchEntryException();
        } catch (IOException | JsonIOException | JsonSyntaxException e) {
            throw new DatabaseException(e);
        }

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
            filenames = this.submissionsDirectory.list();
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
    public @NotNull Optional<Form> getForm(int id) throws DatabaseException {
        Form cachedForm = this.cache.get(id);
        if (cachedForm != null)
            return Optional.of(cachedForm);

        File file = new File(this.submissionsDirectory, id + ".json");

        try (
                CloseableLock ignored = this.lock.read();
                FileReader reader = new FileReader(file)
        ) {
            JsonObject json = GSON.fromJson(reader, JsonObject.class);

            long authorId = json.get("author").getAsLong();
            User author = this.obelisk.getUserDAO().getUser(authorId).orElseThrow(() -> new NoSuchEntryException("Author does not exist"));

            String template = json.get("template").getAsString();

            JsonArray elementJson = json.getAsJsonArray("elements");
            List<FormElement> elements = FormParser.fromJson(elementJson);

            Form form = new Form(id, this, author, template, elements);
            this.cache.put(id, form);
            return Optional.of(form);
        } catch (FileNotFoundException e) {
            return Optional.empty();
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

        File file = new File(this.submissionsDirectory, form.getId() + ".json");

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
            File file = new File(this.submissionsDirectory, id + ".json");
            file.delete();
        }

        // just to be sure
        this.cache.remove(id);
    }
}
