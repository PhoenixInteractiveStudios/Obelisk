package org.burrow_studios.obelisk.api.entity;

import org.burrow_studios.obelisk.api.entity.dao.FormDAO;
import org.burrow_studios.obelisk.api.form.FormElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class Form {
    private final int id;
    private final FormDAO dao;
    private final User author;
    private final String template;
    private final List<FormElement> elements;

    public Form(int id, @NotNull FormDAO dao, @NotNull User author, @NotNull String template, @NotNull List<FormElement> elements) {
        this.id = id;
        this.dao = dao;
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
        this.dao.updateForm(this);
    }

    public void delete() {
        this.dao.deleteForm(this.id);
    }
}
