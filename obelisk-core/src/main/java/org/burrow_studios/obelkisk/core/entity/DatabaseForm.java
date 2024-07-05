package org.burrow_studios.obelkisk.core.entity;

import org.burrow_studios.obelisk.api.entity.Form;
import org.burrow_studios.obelkisk.core.db.interfaces.FormDB;
import org.burrow_studios.obelisk.api.form.FormElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DatabaseForm implements Form {
    private final int id;
    private final FormDB database;
    private final DatabaseUser author;
    private final String template;
    private final List<FormElement> elements;

    public DatabaseForm(int id, @NotNull FormDB database, @NotNull DatabaseUser author, @NotNull String template, @NotNull List<FormElement> elements) {
        this.id = id;
        this.database = database;
        this.author = author;
        this.template = template;
        this.elements = List.copyOf(elements);
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public @NotNull DatabaseUser getAuthor() {
        return this.author;
    }

    @Override
    public @NotNull String getTemplate() {
        return this.template;
    }

    @Override
    public @NotNull List<FormElement> getElements() {
        return this.elements;
    }

    @Override
    public void update() {
        this.database.updateForm(this);
    }

    @Override
    public void delete() {
        this.database.deleteForm(this.id);
    }
}
