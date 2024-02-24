package org.burrow_studios.obelisk.commons.yaml;

public final class YamlNull extends YamlElement {
    public static final YamlNull INSTANCE = new YamlNull();

    public YamlNull() { }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof YamlNull;
    }

    @Override
    Object toObject() {
        return null;
    }
}
