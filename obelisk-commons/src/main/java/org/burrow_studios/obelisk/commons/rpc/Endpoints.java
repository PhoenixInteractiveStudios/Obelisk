package org.burrow_studios.obelisk.commons.rpc;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Stream;

public final class Endpoints {
    private Endpoints() { }

    public static final Endpoint LOGIN      = Endpoint.build(Method.POST  , "/session/:long"      );
    public static final Endpoint LOGOUT     = Endpoint.build(Method.DELETE, "/session/:long/:long");
    public static final Endpoint LOGOUT_ALL = Endpoint.build(Method.DELETE, "/session/:long"      );

    public static final Endpoint GET_SOCKET = Endpoint.build(Method.GET, "/socket");

    /** Shelly-specific endpoints (Internal use only) */
    public static final class Shelly {
        public static final Endpoint GET_PUBLIC_IDENTITY_KEY = Endpoint.build(Method.GET, "/keys/identity/:long");
        public static final Endpoint GET_PUBLIC_SESSION_KEY  = Endpoint.build(Method.GET, "/keys/session"       );
    }

    public static final class Group {
        public static final Endpoint GET_ALL    = Endpoint.build(Method.GET   , "/groups"                    );
        public static final Endpoint GET        = Endpoint.build(Method.GET   , "/groups/:long"              );
        public static final Endpoint CREATE     = Endpoint.build(Method.POST  , "/groups"                    );
        public static final Endpoint ADD_MEMBER = Endpoint.build(Method.PUT   , "/groups/:long/members/:long");
        public static final Endpoint DEL_MEMBER = Endpoint.build(Method.DELETE, "/groups/:long/members/:long");
        public static final Endpoint DELETE     = Endpoint.build(Method.DELETE, "/groups/:long"              );
        public static final Endpoint EDIT       = Endpoint.build(Method.PATCH , "/groups/:long"              );
    }

    public static final class Project {
        public static final Endpoint GET_ALL    = Endpoint.build(Method.GET   , "/projects"                    );
        public static final Endpoint GET        = Endpoint.build(Method.GET   , "/projects/:long"              );
        public static final Endpoint CREATE     = Endpoint.build(Method.POST  , "/projects"                    );
        public static final Endpoint ADD_MEMBER = Endpoint.build(Method.PUT   , "/projects/:long/members/:long");
        public static final Endpoint DEL_MEMBER = Endpoint.build(Method.DELETE, "/projects/:long/members/:long");
        public static final Endpoint DELETE     = Endpoint.build(Method.DELETE, "/projects/:long"              );
        public static final Endpoint EDIT       = Endpoint.build(Method.PATCH , "/projects/:long"              );
    }

    public static final class Ticket {
        public static final Endpoint GET_ALL  = Endpoint.build(Method.GET   , "/tickets"                  );
        public static final Endpoint GET      = Endpoint.build(Method.GET   , "/tickets/:long"            );
        public static final Endpoint CREATE   = Endpoint.build(Method.POST  , "/tickets"                  );
        public static final Endpoint ADD_USER = Endpoint.build(Method.PUT   , "/tickets/:long/users/:long");
        public static final Endpoint DEL_USER = Endpoint.build(Method.DELETE, "/tickets/:long/users/:long");
        public static final Endpoint DELETE   = Endpoint.build(Method.DELETE, "/tickets/:long"            );
        public static final Endpoint EDIT     = Endpoint.build(Method.PATCH , "/tickets/:long"            );
    }

    public static final class User {
        public static final Endpoint GET_ALL = Endpoint.build(Method.GET   , "/users"      );
        public static final Endpoint GET     = Endpoint.build(Method.GET   , "/users/:long");
        public static final Endpoint CREATE  = Endpoint.build(Method.POST  , "/users"      );
        public static final Endpoint DELETE  = Endpoint.build(Method.DELETE, "/users/:long");
        public static final Endpoint EDIT    = Endpoint.build(Method.PATCH , "/users/:long");
    }

    public static final class Board {
        public static final Endpoint GET_ALL = Endpoint.build(Method.GET   , "/boards"      );
        public static final Endpoint GET     = Endpoint.build(Method.GET   , "/boards/:long");
        public static final Endpoint CREATE  = Endpoint.build(Method.POST  , "/boards"      );
        public static final Endpoint DELETE  = Endpoint.build(Method.DELETE, "/boards/:long");
        public static final Endpoint EDIT    = Endpoint.build(Method.PATCH , "/boards/:long");

        public static final class Tag {
            public static final Endpoint GET_ALL = Endpoint.build(Method.GET   , "/boards/:long/tags"      );
            public static final Endpoint GET     = Endpoint.build(Method.GET   , "/boards/:long/tags/:long");
            public static final Endpoint CREATE  = Endpoint.build(Method.POST  , "/boards/:long/tags"      );
            public static final Endpoint DELETE  = Endpoint.build(Method.DELETE, "/boards/:long/tags/:long");
            public static final Endpoint EDIT    = Endpoint.build(Method.PATCH , "/boards/:long/tags/:long");
        }

        public static final class Issue {
            public static final Endpoint GET_ALL      = Endpoint.build(Method.GET   , "/boards/:long/issues"                      );
            public static final Endpoint GET          = Endpoint.build(Method.GET   , "/boards/:long/issues/:long"                );
            public static final Endpoint CREATE       = Endpoint.build(Method.POST  , "/boards/:long/issues"                      );
            public static final Endpoint ADD_ASSIGNEE = Endpoint.build(Method.PUT   , "/boards/:long/issues/:long/assignees/:long");
            public static final Endpoint DEL_ASSIGNEE = Endpoint.build(Method.DELETE, "/boards/:long/issues/:long/assignees/:long");
            public static final Endpoint ADD_TAG      = Endpoint.build(Method.PUT   , "/boards/:long/issues/:long/tags/:long"     );
            public static final Endpoint DEL_TAG      = Endpoint.build(Method.DELETE, "/boards/:long/issues/:long/tags/:long"     );
            public static final Endpoint DELETE       = Endpoint.build(Method.DELETE, "/boards/:long/issues/:long"                );
            public static final Endpoint EDIT         = Endpoint.build(Method.PATCH , "/boards/:long/issues/:long"                );
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
