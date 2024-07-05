package org.burrow_studios.obelkisk.server.db.interfaces;

import org.burrow_studios.obelkisk.server.entity.DatabaseForm;
import org.burrow_studios.obelkisk.server.entity.DatabaseUser;
import org.burrow_studios.obelkisk.server.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface FormDB {
    @NotNull DatabaseForm createForm(@NotNull DatabaseUser author, @NotNull String template) throws DatabaseException;


    @NotNull List<Integer> listForms() throws DatabaseException;

    @NotNull DatabaseForm getForm(int id) throws DatabaseException;


    void updateForm(@NotNull DatabaseForm form) throws DatabaseException;


    void deleteForm(int id) throws DatabaseException;
}
