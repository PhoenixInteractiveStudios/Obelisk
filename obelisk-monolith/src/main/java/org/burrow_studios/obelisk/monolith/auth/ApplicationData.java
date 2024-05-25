package org.burrow_studios.obelisk.monolith.auth;

import java.util.Set;

public class ApplicationData {
    private final long id;
    private final String name;
    private final String pubKey;
    private final Set<Intent> intents;

    public ApplicationData(long id, String name, String pubKey, Set<Intent> intents) {
        this.id = id;
        this.name = name;
        this.pubKey = pubKey;
        this.intents = Set.copyOf(intents);
    }
}
