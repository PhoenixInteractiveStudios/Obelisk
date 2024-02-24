package org.burrow_studios.obelisk.commons.yaml;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;

public final class YamlSection extends YamlElement {
    private final Map<String, YamlElement> members = new LinkedHashMap<>();;

    public YamlSection() { }

    public YamlElement get(@NotNull String key) {
        return members.get(key);
    }

    public YamlPrimitive getAsPrimitive(@NotNull String key) {
        return (YamlPrimitive) members.get(key);
    }

    public YamlSection getAsSection(@NotNull String key) {
        return (YamlSection) members.get(key);
    }

    public YamlList getAsList(@NotNull String key) {
        return (YamlList) members.get(key);
    }

    public void set(@NotNull String key, YamlElement element) {
        members.put(key, element);
    }

    public void set(@NotNull String key, Boolean value) {
        members.put(key, value != null ? new YamlPrimitive(value) : YamlNull.INSTANCE);
    }

    public void set(@NotNull String key, Number value) {
        members.put(key, value != null ? new YamlPrimitive(value) : YamlNull.INSTANCE);
    }

    public void set(@NotNull String key, String value) {
        members.put(key, value != null ? new YamlPrimitive(value) : YamlNull.INSTANCE);
    }

    public void save(@NotNull Writer writer) {
        YamlUtil.save(this, writer);
    }

    public void save(@NotNull OutputStream stream) {
        YamlUtil.save(this, stream);
    }

    public void save(@NotNull File file) throws IOException {
        YamlUtil.save(this, file);
    }

    static @NotNull YamlSection parse(@NotNull Map<String, Object> map) {
        YamlSection section = new YamlSection();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            YamlElement value = YamlUtil.parse(entry.getValue());
            section.set(entry.getKey(), value);
        }
        return section;
    }

    @Override
    Object toObject() {
        LinkedHashMap<String, Object> objectMap = new LinkedHashMap<>();
        for (Map.Entry<String, YamlElement> entry : members.entrySet())
            objectMap.put(entry.getKey(), entry.getValue().toObject());
        return objectMap;
    }
}
