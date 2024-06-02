package org.burrow_studios.obelisk.util;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    private StringUtil() { }

    public static @NotNull String replaceNamedWithNumberedParameters(@NotNull String str) {
        Pattern pattern = Pattern.compile("\\{([^}]+)\\}");
        Matcher matcher = pattern.matcher(str);

        Map<String, Integer> params = new HashMap<>();
        int index = 0;
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String parameter = matcher.group();

            if (!params.containsKey(parameter))
                params.put(parameter, index++);

            matcher.appendReplacement(result, "{" + params.get(parameter) + "}");
        }
        matcher.appendTail(result);

        return result.toString();
    }
}
