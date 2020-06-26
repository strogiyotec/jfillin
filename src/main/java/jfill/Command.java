package jfill;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

final class Command implements Callable<Void> {

    private final Callable<String> command;

    Command(
            final Pattern pattern,
            final String[] args,
            final Map<String, Map<String, String>> values,
            final String defaultTag
    ) {
        this.command = () -> {
            var resolvedArgs = new ArrayList<String>(args.length);
            for (var arg : args) {
                var matcher = pattern.matcher(arg);
                if (matcher.find()) {
                    var key = matcher.group(1);
                    var split = key.split(":");
                    //doesn't have tag
                    if (split.length == 1) {
                        resolvedArgs.add(values.get(defaultTag).get(key));
                    } else {
                        resolvedArgs.add(values.get(split[0]).get(split[1]).trim());
                    }
                } else {
                    resolvedArgs.add(arg);
                }
            }
            var process = new ProcessBuilder()
                    .command(resolvedArgs)
                    .inheritIO()
                    .start();
            process.waitFor();
            return null;
        };
    }

    @Override
    public Void call() throws Exception {
        this.command.call();
        return null;
    }
}
