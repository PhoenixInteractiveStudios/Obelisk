package org.burrow_studios.obelisk.commons.yaml;

import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class YamlUtil {
    static final Yaml YAML = new Yaml();

    private YamlUtil() { }

    public static @NotNull YamlElement parse(String yaml) {
        Iterable<Object> elements = YAML.loadAll(yaml);
        return parse(elements);
    }

    public static <T extends YamlElement> @NotNull T parse(String yaml, @NotNull Class<T> type) {
        return type.cast(parse(yaml));
    }

    private static @NotNull YamlElement parse(@NotNull Iterable<Object> elements) {
        ArrayList<YamlElement> targetElements = new ArrayList<>();
        for (Object sourceElement : elements)
            targetElements.add(parse(sourceElement));

        if (targetElements.isEmpty())
            return YamlNull.INSTANCE;
        if (targetElements.size() == 1)
            return targetElements.get(0);

        YamlList list = new YamlList();
        list.addAll(targetElements);
        return list;
    }

    @SuppressWarnings("unchecked") // safe in the context of SnakeYAML
    static @NotNull YamlElement parse(Object value) {
        if (value == null)
            return YamlNull.INSTANCE;
        else if (value instanceof Boolean b)
            return new YamlPrimitive(b);
        else if (value instanceof Number n)
            return new YamlPrimitive(n);
        else if (value instanceof String s)
            return new YamlPrimitive(s);
        else if (value instanceof Map<?, ?>)
            return YamlSection.parse((Map<String, Object>) value);
        else if (value instanceof List<?>)
            return YamlList.parse((List<Object>) value);
        else
            throw new YamlFormatException("Unknown type: " + value.getClass().getName());
    }

    public static @NotNull YamlElement load(@NotNull File file) throws FileNotFoundException {
        Iterable<Object> elements = YAML.loadAll(new FileReader(file));
        return parse(elements);
    }

    public static <T extends YamlElement> @NotNull T load(@NotNull File file, @NotNull Class<T> type) throws FileNotFoundException {
        return type.cast(load(file));
    }

    public static void saveDefault(@NotNull File file, InputStream resource) throws IOException {
        if (resource == null)
            throw new IllegalArgumentException("Resource is null");

        if (!file.exists()) {
            boolean created = file.createNewFile();

            if (!created)
                throw new IOException("File could not be created");

            Files.copy(resource, file.toPath());
        }
    }
}
