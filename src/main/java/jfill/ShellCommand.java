package jfill;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

final class ShellCommand {

    private final Callable<String> command;

    ShellCommand(
            final Pattern pattern,
            final String[] args,
            final ValuesStorage storage,
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
                        resolvedArgs.add(storage.get(defaultTag, key));
                    } else {
                        resolvedArgs.add(storage.get(split[0], split[1]));
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

    void execute() throws Exception {
        this.command.call();
    }
}
