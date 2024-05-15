package org.burrow_studios.obelisk.core.http;

import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

import static org.burrow_studios.obelisk.core.http.Method.*;

public class Route {
    public static class Meta {
        public static final Route GET_GATEWAY = new Route(GET, "gateway");
    }

    public static class User {
        public static final Route CREATE_USER = new Route(POST, "users");
        public static final Route DELETE_USER = new Route(DELETE, "users/{user_id}");
        public static final Route EDIT_USER   = new Route(PATCH, "users/{user_id}");
    }

    public static class Discord {
        public static final Route CREATE_DISCORD_ACCOUNT = new Route(POST, "accounts/discord");
        public static final Route DELETE_DISCORD_ACCOUNT = new Route(DELETE, "accounts/discord/{discord_account_id}");
        public static final Route EDIT_DISCORD_ACCOUNT   = new Route(PATCH, "accounts/discord/{discord_account_id}");
    }

    public static class Minecraft {
        public static final Route CREATE_MINECRAFT_ACCOUNT = new Route(POST, "accounts/minecraft");
        public static final Route DELETE_MINECRAFT_ACCOUNT = new Route(DELETE, "accounts/minecraft/{minecraft_account_id}");
        public static final Route EDIT_MINECRAFT_ACCOUNT   = new Route(PATCH, "accounts/minecraft/{minecraft_account_id}");
    }

    public static class Ticket {
        public static final Route CREATE_TICKET = new Route(POST, "tickets");
        public static final Route DELETE_TICKET = new Route(DELETE, "tickets/{ticket_id}");
        public static final Route EDIT_TICKET   = new Route(PATCH, "tickets/{ticket_id}");
        public static final Route ADD_USER      = new Route(PUT, "tickets/{ticket_id}/users/{user_id}");
        public static final Route REMOVE_USER   = new Route(DELETE, "tickets/{ticket_id}/users/{user_id}");
    }

    public static class Project {
        public static final Route CREATE_PROJECT = new Route(POST, "projects");
        public static final Route DELETE_PROJECT = new Route(DELETE, "projects/{project_id}");
        public static final Route EDIT_PROJECT   = new Route(PATCH, "projects/{project_id}");
        public static final Route ADD_MEMBER     = new Route(PUT, "projects/{project_id}/members/{user_id}");
        public static final Route REMOVE_MEMBER  = new Route(DELETE, "projects/{project_id}/members/{user_id}");
    }

    /* - - - - - - - - - - */

    private final Method method;
    private final Path path;
    private final int params;

    private Route(@NotNull Method method, @NotNull String path) {
        this.method = method;
        this.path = new Path(path);

        this.params = this.path.getParamCount();
    }

    public @NotNull Compiled compile(@NotNull Object... args) {
        if (args.length != params)
            throw new IllegalArgumentException("Incorrect amount of parameters");

        String compiled = MessageFormat.format(this.path.getPath(), args);

        return new Compiled(this, compiled);
    }

    public @NotNull Method getMethod() {
        return this.method;
    }

    public @NotNull Path getPath() {
        return this.path;
    }

    public static @NotNull Route custom(@NotNull Method method, @NotNull String path) {
        return new Route(method, path);
    }

    public static class Compiled {
        private final Route base;
        private final Path path;

        private Compiled(Route base, String compiled) {
            this.base = base;
            this.path = new Path(compiled);
        }

        public @NotNull Route getBase() {
            return this.base;
        }

        public @NotNull Path getPath() {
            return this.path;
        }
    }
}
