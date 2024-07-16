package org.burrow_studios.obelisk.api.entity.dao;

import org.burrow_studios.obelisk.api.FormTemplateMeta;
import org.burrow_studios.obelisk.api.entity.Form;
import org.burrow_studios.obelisk.api.entity.User;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface FormDAO {
    @NotNull Form createForm(@NotNull User author, long channelId, @NotNull String template);

    @NotNull List<FormTemplateMeta> listTemplates();
    @NotNull List<Integer> listForms();
    @NotNull Optional<Form> getForm(int id);

    void updateForm(@NotNull Form form);

    void deleteForm(int id);
}
