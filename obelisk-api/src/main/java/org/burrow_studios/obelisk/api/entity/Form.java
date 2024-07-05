package org.burrow_studios.obelisk.api.entity;

import org.burrow_studios.obelisk.api.form.FormElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Form {
    int getId();
    @NotNull User getAuthor();
    @NotNull String getTemplate();
    @NotNull List<? extends FormElement> getElements();

    void update();

    void delete();
}
