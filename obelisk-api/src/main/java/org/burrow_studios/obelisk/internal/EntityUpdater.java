package org.burrow_studios.obelisk.internal;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.internal.entities.GroupImpl;
import org.burrow_studios.obelisk.internal.entities.ProjectImpl;
import org.burrow_studios.obelisk.internal.entities.TicketImpl;
import org.burrow_studios.obelisk.internal.entities.UserImpl;
import org.burrow_studios.obelisk.internal.entities.board.BoardImpl;
import org.burrow_studios.obelisk.internal.entities.board.IssueImpl;
import org.burrow_studios.obelisk.internal.entities.board.TagImpl;
import org.jetbrains.annotations.NotNull;

public class EntityUpdater {
    private EntityUpdater() { }

    public static void updateGroup(@NotNull GroupImpl group, @NotNull JsonObject json) {
        // TODO
    }

    public static void updateProject(@NotNull ProjectImpl project, @NotNull JsonObject json) {
        // TODO
    }

    public static void updateTicket(@NotNull TicketImpl ticket, @NotNull JsonObject json) {
        // TODO
    }

    public static void updateUser(@NotNull UserImpl user, @NotNull JsonObject json) {
        // TODO
    }

    public static void updateBoard(@NotNull BoardImpl board, @NotNull JsonObject json) {
        // TODO
    }

    public static void updateIssue(@NotNull IssueImpl issue, @NotNull JsonObject json) {
        // TODO
    }

    public static void updateTag(@NotNull TagImpl tag, @NotNull JsonObject json) {
        // TODO
    }
}
