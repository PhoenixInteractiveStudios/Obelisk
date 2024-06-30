package org.burrow_studios.obelkisk.core.entity;

import org.burrow_studios.obelkisk.core.db.interfaces.FormDB;
import org.burrow_studios.obelkisk.core.form.FormElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Form {
    private final int id;
    private final FormDB database;
    private final User author;
    private final String template;
    private final List<FormElement> elements;

    public Form(int id, @NotNull FormDB database, @NotNull User author, @NotNull String template, @NotNull List<FormElement> elements) {
        this.id = id;
        this.database = database;
        this.author = author;
        this.template = template;
        this.elements = List.copyOf(elements);
    }

    public int getId() {
        return this.id;
    }

    public @NotNull User getAuthor() {
        return this.author;
    }

    public @NotNull String getTemplate() {
        return this.template;
    }

    public @NotNull List<FormElement> getElements() {
        return this.elements;
    }

    public void update() {
        this.database.updateForm(this);
    }

    public void delete() {
        this.database.deleteForm(this.id);
    }
}
