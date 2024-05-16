package org.burrow_studios.obelisk.client.entities;

import com.google.gson.JsonElement;
import org.burrow_studios.obelisk.api.action.Action;
import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.client.ObeliskImpl;
import org.burrow_studios.obelisk.client.action.ActionImpl;
import org.burrow_studios.obelisk.client.action.DeleteActionImpl;
import org.burrow_studios.obelisk.client.action.entity.project.ProjectModifierImpl;
import org.burrow_studios.obelisk.core.cache.OrderedEntitySetView;
import org.burrow_studios.obelisk.core.entities.AbstractProject;
import org.burrow_studios.obelisk.core.entities.AbstractUser;
import org.burrow_studios.obelisk.core.http.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ProjectImpl extends AbstractProject {
    public ProjectImpl(
            @NotNull ObeliskImpl obelisk,
            long id,
            @NotNull String title,
            @NotNull State state,
            @NotNull OrderedEntitySetView<AbstractUser> members
    ) {
        super(obelisk, id, title, state, members);
    }

    @Override
    public @NotNull ProjectModifierImpl modify() {
        return new ProjectModifierImpl(this);
    }

    @Override
    public @NotNull DeleteAction<Project> delete() {
        Route.Compiled route = Route.Project.DELETE_PROJECT.compile(this.id);

        return new DeleteActionImpl<>(((ObeliskImpl) this.getAPI()), route, this.getId(), Project.class);
    }

    @Override
    public @NotNull Action<Void> addUser(@NotNull User user) {
        Route.Compiled route = Route.Project.ADD_MEMBER.compile(this.id, user.getId());

        return new ActionImpl<>(this.getAPI(), route) {
            @Override
            protected @Nullable JsonElement getRequestBody() {
                return null;
            }
        };
    }

    @Override
    public @NotNull Action<Void> removeUser(@NotNull User user) {
        Route.Compiled route = Route.Project.REMOVE_MEMBER.compile(this.id, user.getId());

        return new ActionImpl<>(this.getAPI(), route) {
            @Override
            protected @Nullable JsonElement getRequestBody() {
                return null;
            }
        };
    }
}
