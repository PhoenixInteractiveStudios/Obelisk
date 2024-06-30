package org.burrow_studios.obelkisk.core.db.interfaces;

import org.burrow_studios.obelkisk.core.entity.Form;
import org.burrow_studios.obelkisk.core.entity.User;
import org.burrow_studios.obelkisk.core.exceptions.DatabaseException;
import org.burrow_studios.obelkisk.core.form.FormElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface FormDB {
    @NotNull Form createForm(@NotNull User author, @NotNull String template, @NotNull List<FormElement> elements) throws DatabaseException;


    @NotNull List<Integer> listForms() throws DatabaseException;

    @NotNull Form getForm(int id) throws DatabaseException;


    void updateForm(@NotNull Form form) throws DatabaseException;


    void deleteForm(int id) throws DatabaseException;
}
