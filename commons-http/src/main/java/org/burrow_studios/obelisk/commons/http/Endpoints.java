package org.burrow_studios.obelisk.commons.http;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Stream;

public final class Endpoints {
    private Endpoints() { }

    public static final Endpoint LOGIN      = new Endpoint(Method.POST  , "/session/:long"      , AuthLevel.IDENTITY);
    public static final Endpoint LOGOUT     = new Endpoint(Method.DELETE, "/session/:long/:long", AuthLevel.SESSION);
    public static final Endpoint LOGOUT_ALL = new Endpoint(Method.DELETE, "/session/:long"      , AuthLevel.IDENTITY);

    public static final Endpoint GET_SOCKET = new Endpoint(Method.GET, "/socket", AuthLevel.NONE);

    /** Shelly-specific endpoints (Internal use only) */
    public static final class Shelly {
        public static final Endpoint GET_PUBLIC_IDENTITY_KEY = new Endpoint(Method.GET, "/keys/identity/:long", AuthLevel.NONE);
        public static final Endpoint GET_PUBLIC_SESSION_KEY  = new Endpoint(Method.GET, "/keys/session"       , AuthLevel.NONE);
    }

    public static final class Group {
        public static final Endpoint GET_ALL    = new Endpoint(Method.GET   , "/groups"                    , AuthLevel.SESSION);
        public static final Endpoint GET        = new Endpoint(Method.GET   , "/groups/:long"              , AuthLevel.SESSION);
        public static final Endpoint CREATE     = new Endpoint(Method.POST  , "/groups"                    , AuthLevel.SESSION);
        public static final Endpoint ADD_MEMBER = new Endpoint(Method.PUT   , "/groups/:long/members/:long", AuthLevel.SESSION);
        public static final Endpoint DEL_MEMBER = new Endpoint(Method.DELETE, "/groups/:long/members/:long", AuthLevel.SESSION);
        public static final Endpoint DELETE     = new Endpoint(Method.DELETE, "/groups/:long"              , AuthLevel.SESSION);
        public static final Endpoint EDIT       = new Endpoint(Method.PATCH , "/groups/:long"              , AuthLevel.SESSION);
    }

    public static final class Project {
        public static final Endpoint GET_ALL    = new Endpoint(Method.GET   , "/projects"                    , AuthLevel.SESSION);
        public static final Endpoint GET        = new Endpoint(Method.GET   , "/projects/:long"              , AuthLevel.SESSION);
        public static final Endpoint CREATE     = new Endpoint(Method.POST  , "/projects"                    , AuthLevel.SESSION);
        public static final Endpoint ADD_MEMBER = new Endpoint(Method.PUT   , "/projects/:long/members/:long", AuthLevel.SESSION);
        public static final Endpoint DEL_MEMBER = new Endpoint(Method.DELETE, "/projects/:long/members/:long", AuthLevel.SESSION);
        public static final Endpoint DELETE     = new Endpoint(Method.DELETE, "/projects/:long"              , AuthLevel.SESSION);
        public static final Endpoint EDIT       = new Endpoint(Method.PATCH , "/projects/:long"              , AuthLevel.SESSION);
    }

    public static final class Ticket {
        public static final Endpoint GET_ALL  = new Endpoint(Method.GET   , "/tickets"                  , AuthLevel.SESSION);
        public static final Endpoint GET      = new Endpoint(Method.GET   , "/tickets/:long"            , AuthLevel.SESSION);
        public static final Endpoint CREATE   = new Endpoint(Method.POST  , "/tickets"                  , AuthLevel.SESSION);
        public static final Endpoint ADD_USER = new Endpoint(Method.PUT   , "/tickets/:long/users/:long", AuthLevel.SESSION);
        public static final Endpoint DEL_USER = new Endpoint(Method.DELETE, "/tickets/:long/users/:long", AuthLevel.SESSION);
        public static final Endpoint DELETE   = new Endpoint(Method.DELETE, "/tickets/:long"            , AuthLevel.SESSION);
        public static final Endpoint EDIT     = new Endpoint(Method.PATCH , "/tickets/:long"            , AuthLevel.SESSION);
    }

    public static final class User {
        public static final Endpoint GET_ALL = new Endpoint(Method.GET   , "/users"      , AuthLevel.SESSION);
        public static final Endpoint GET     = new Endpoint(Method.GET   , "/users/:long", AuthLevel.SESSION);
        public static final Endpoint CREATE  = new Endpoint(Method.POST  , "/users"      , AuthLevel.SESSION);
        public static final Endpoint DELETE  = new Endpoint(Method.DELETE, "/users/:long", AuthLevel.SESSION);
        public static final Endpoint EDIT    = new Endpoint(Method.PATCH , "/users/:long", AuthLevel.SESSION);
    }

    public static final class Board {
        public static final Endpoint GET_ALL = new Endpoint(Method.GET   , "/boards"      , AuthLevel.SESSION);
        public static final Endpoint GET     = new Endpoint(Method.GET   , "/boards/:long", AuthLevel.SESSION);
        public static final Endpoint CREATE  = new Endpoint(Method.POST  , "/boards"      , AuthLevel.SESSION);
        public static final Endpoint DELETE  = new Endpoint(Method.DELETE, "/boards/:long", AuthLevel.SESSION);
        public static final Endpoint EDIT    = new Endpoint(Method.PATCH , "/boards/:long", AuthLevel.SESSION);

        public static final class Tag {
            public static final Endpoint GET_ALL = new Endpoint(Method.GET   , "/boards/:long/tags"      , AuthLevel.SESSION);
            public static final Endpoint GET     = new Endpoint(Method.GET   , "/boards/:long/tags/:long", AuthLevel.SESSION);
            public static final Endpoint CREATE  = new Endpoint(Method.POST  , "/boards/:long/tags"      , AuthLevel.SESSION);
            public static final Endpoint DELETE  = new Endpoint(Method.DELETE, "/boards/:long/tags/:long", AuthLevel.SESSION);
            public static final Endpoint EDIT    = new Endpoint(Method.PATCH , "/boards/:long/tags/:long", AuthLevel.SESSION);
        }

        public static final class Issue {
            public static final Endpoint GET_ALL      = new Endpoint(Method.GET   , "/boards/:long/issues"                      , AuthLevel.SESSION);
            public static final Endpoint GET          = new Endpoint(Method.GET   , "/boards/:long/issues/:long"                , AuthLevel.SESSION);
            public static final Endpoint CREATE       = new Endpoint(Method.POST  , "/boards/:long/issues"                      , AuthLevel.SESSION);
            public static final Endpoint ADD_ASSIGNEE = new Endpoint(Method.PUT   , "/boards/:long/issues/:long/assignees/:long", AuthLevel.SESSION);
            public static final Endpoint DEL_ASSIGNEE = new Endpoint(Method.DELETE, "/boards/:long/issues/:long/assignees/:long", AuthLevel.SESSION);
            public static final Endpoint ADD_TAG      = new Endpoint(Method.PUT   , "/boards/:long/issues/:long/tags/:long"     , AuthLevel.SESSION);
            public static final Endpoint DEL_TAG      = new Endpoint(Method.DELETE, "/boards/:long/issues/:long/tags/:long"     , AuthLevel.SESSION);
            public static final Endpoint DELETE       = new Endpoint(Method.DELETE, "/boards/:long/issues/:long"                , AuthLevel.SESSION);
            public static final Endpoint EDIT         = new Endpoint(Method.PATCH , "/boards/:long/issues/:long"                , AuthLevel.SESSION);
        }
    }

    public static Endpoint[] getAuthEndpoints() {
        return new Endpoint[]{ LOGIN, LOGOUT, LOGOUT_ALL, GET_SOCKET };
    }

    public static Endpoint[] getEntityEndpoints() {
        return Stream.of(
                        getEndpoints(Group.class),
                        getEndpoints(Project.class),
                        getEndpoints(Ticket.class),
                        getEndpoints(User.class),
                        getEndpoints(Board.class),
                        getEndpoints(Board.Tag.class),
                        getEndpoints(Board.Issue.class)
                )
                .mapMulti(Stream::forEach)
                .map(Endpoint.class::cast)
                .toArray(Endpoint[]::new);
    }

    private static Stream<Endpoint> getEndpoints(@NotNull Class<?> cls) {
        return Arrays.stream(cls.getFields())
                .filter(field -> Modifier.isPublic(field.getModifiers()))
                .filter(field -> Modifier.isStatic(field.getModifiers()))
                .filter(field -> Modifier.isFinal(field.getModifiers()))
                .filter(field -> field.getType().equals(Endpoint.class))
                .map(Endpoints::getStaticFieldValue)
                .map(Endpoint.class::cast);
    }

    private static Object getStaticFieldValue(@NotNull Field field) {
        try {
            return field.get(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
