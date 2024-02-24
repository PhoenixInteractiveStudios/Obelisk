package org.burrow_studios.obelisk.commons.yaml;

import org.jetbrains.annotations.NotNull;

public sealed abstract class YamlElement permits YamlList, YamlNull, YamlPrimitive, YamlSection {
    public @NotNull YamlSection getAsSection() {
        return (YamlSection) this;
    }

    public @NotNull YamlList getAsList() {
        return (YamlList) this;
    }

    public @NotNull YamlPrimitive getAsPrimitive() {
        return (YamlPrimitive) this;
    }
}
