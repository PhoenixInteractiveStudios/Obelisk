package org.burrow_studios.obelisk.internal.net.http;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public final class Route {
    public static final Route LOGIN      = new Route(Method.POST  , "/session/%s");
    public static final Route LOGOUT     = new Route(Method.DELETE, "/session/%s/%s");
    public static final Route LOGOUT_ALL = new Route(Method.DELETE, "/session/%s");

    public static final class Group {
        public static final Route GET_ALL    = new Route(Method.GET   , "/groups");
        public static final Route GET        = new Route(Method.GET   , "/groups/%s");
        public static final Route CREATE     = new Route(Method.POST  , "/groups");
        public static final Route ADD_MEMBER = new Route(Method.PUT   , "/groups/%s/members/%s");
        public static final Route DEL_MEMBER = new Route(Method.DELETE, "/groups/%s/members/%s");
        public static final Route DELETE     = new Route(Method.DELETE, "/groups/%s");
        public static final Route EDIT       = new Route(Method.PATCH , "/groups/%s");
    }

    public static final class Project {
        public static final Route GET_ALL    = new Route(Method.GET   , "/projects");
        public static final Route GET        = new Route(Method.GET   , "/projects/%s");
        public static final Route CREATE     = new Route(Method.POST  , "/projects");
        public static final Route ADD_MEMBER = new Route(Method.PUT   , "/projects/%s/members/%s");
        public static final Route DEL_MEMBER = new Route(Method.DELETE, "/projects/%s/members/%s");
        public static final Route DELETE     = new Route(Method.DELETE, "/projects/%s");
        public static final Route EDIT       = new Route(Method.PATCH , "/projects/%s");
    }

    public static final class Ticket {
        public static final Route GET_ALL  = new Route(Method.GET   , "/tickets");
        public static final Route GET      = new Route(Method.GET   , "/tickets/%s");
        public static final Route CREATE   = new Route(Method.POST  , "/tickets");
        public static final Route ADD_USER = new Route(Method.PUT   , "/tickets/%s/users/%s");
        public static final Route DEL_USER = new Route(Method.DELETE, "/tickets/%s/users/%s");
        public static final Route DELETE   = new Route(Method.DELETE, "/tickets/%s");
        public static final Route EDIT     = new Route(Method.PATCH , "/tickets/%s");
    }

    public static final class User {
        public static final Route GET_ALL = new Route(Method.GET   , "/users");
        public static final Route GET     = new Route(Method.GET   , "/users/%s");
        public static final Route CREATE  = new Route(Method.POST  , "/users");
        public static final Route DELETE  = new Route(Method.DELETE, "/users/%s");
        public static final Route EDIT    = new Route(Method.PATCH , "/users/%s");
    }

    public static final class Board {
        public static final Route GET_ALL = new Route(Method.GET   , "/boards");
        public static final Route GET     = new Route(Method.GET   , "/boards/%s");
        public static final Route CREATE  = new Route(Method.POST  , "/boards");
        public static final Route DELETE  = new Route(Method.DELETE, "/boards/%s");
        public static final Route EDIT    = new Route(Method.PATCH , "/boards/%s");

        public static final class Tag {
            public static final Route GET_ALL = new Route(Method.GET   , "/boards/%s/tags");
            public static final Route GET     = new Route(Method.GET   , "/boards/%s/tags/%s");
            public static final Route CREATE  = new Route(Method.POST  , "/boards/%s/tags");
            public static final Route DELETE  = new Route(Method.DELETE, "/boards/%s/tags/%s");
            public static final Route EDIT    = new Route(Method.PATCH , "/boards/%s/tags/%s");
        }

        public static final class Issue {
            public static final Route GET_ALL      = new Route(Method.GET   , "/boards/%s/issues");
            public static final Route GET          = new Route(Method.GET   , "/boards/%s/issues/%s");
            public static final Route CREATE       = new Route(Method.POST  , "/boards/%s/issues");
            public static final Route ADD_ASSIGNEE = new Route(Method.PUT   , "/boards/%s/issues/%s/assignees/%s");
            public static final Route DEL_ASSIGNEE = new Route(Method.DELETE, "/boards/%s/issues/%s/assignees/%s");
            public static final Route ADD_TAG      = new Route(Method.PUT   , "/boards/%s/issues/%s/tags/%s");
            public static final Route DEL_TAG      = new Route(Method.DELETE, "/boards/%s/issues/%s/tags/%s");
            public static final Route DELETE       = new Route(Method.DELETE, "/boards/%s/issues/%s");
            public static final Route EDIT         = new Route(Method.PATCH , "/boards/%s/issues/%s");
        }
    }

    /* - - - */

    private final @NotNull Method method;
    private final @NotNull String path;
    private final int paramCount;

    public Route(@NotNull Method method, @NotNull String path) {
        this.method = method;
        this.path   = path;

        this.paramCount = Pattern.compile("(%s)").matcher(path).groupCount();
    }

    public @NotNull RouteBuilder builder() {
        return new RouteBuilder(this);
    }

    public @NotNull Method getMethod() {
        return method;
    }

    public @NotNull String getPath() {
        return path;
    }

    public int getParamCount() {
        return paramCount;
    }
}
