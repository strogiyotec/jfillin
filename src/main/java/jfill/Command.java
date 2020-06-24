package jfill;

import java.util.Map;
import java.util.regex.Pattern;

public final class Command {

    public String fill(
            final Pattern pattern,
            final String[] args,
            final Map<String, Map<String, String>> resolvedValues,
            final String defaultTag
    ) {
        var builder = new StringBuilder();
        for (var arg : args) {
            var matcher = pattern.matcher(arg);
            if (matcher.find()) {
                var key = matcher.group(1);
                var split = key.split(":");
                //doesn't have tag
                if (split.length == 1) {
                    builder.append(resolvedValues.get(defaultTag).get(key)).append(" ");
                } else {
                    builder.append(resolvedValues.get(split[0]).get(split[1])).append(" ");
                }
            } else {
                builder.append(arg).append(" ");
            }
        }
        return builder.toString();
    }
}
