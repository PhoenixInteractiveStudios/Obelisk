package org.burrow_studios.obelisk.commons.yaml;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.*;

public final class YamlList extends YamlElement implements Iterable<YamlElement> {
    private final ArrayList<YamlElement> elements;

    public YamlList() {
        this.elements = new ArrayList<>();
    }

    public YamlList(int initialCapacity) {
        this.elements = new ArrayList<>(initialCapacity);
    }

    public void add(YamlElement element) {
        this.elements.add(element != null ? element : YamlNull.INSTANCE);
    }

    public void addAll(@NotNull Collection<YamlElement> elements) {
        elements.forEach(this::add);
    }

    @Override
    public @NotNull Iterator<YamlElement> iterator() {
        return this.elements.iterator();
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

    static @NotNull YamlList parse(@NotNull List<Object> elements) {
        YamlList list = new YamlList();
        for (Object element : elements)
            list.add(YamlUtil.parse(element));
        return list;
    }

    @Override
    Object toObject() {
        ArrayList<Object> objects = new ArrayList<>();
        for (YamlElement element : elements)
            objects.add(element.toObject());
        return objects;
    }
}
