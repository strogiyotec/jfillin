package jfill;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;

final class ShellCommand {

    private final Callable<Void> command;

    ShellCommand(
            final String[] args,
            final ValuesStorage storage,
            final ProcessBuilder builder
    ) {
        this.command = () -> {
            var resolvedArgs = new ArrayList<String>(args.length);
            for (var arg : args) {
                var matcher = Defaults.FILLIN_PTN.matcher(arg);
                if (matcher.find()) {
                    var key = matcher.group(1);
                    var split = key.split(":");
                    //doesn't have tag
                    if (split.length == 1) {
                        resolvedArgs.add(getValue(arg, storage.get(Defaults.NO_TAG, key), matcher));
                    } else {
                        resolvedArgs.add(getValue(arg, storage.get(split[0], split[1]), matcher));
                    }
                } else {
                    resolvedArgs.add(arg);
                }
            }
            var process = builder
                    .command(resolvedArgs)
                    .start();
            process.waitFor();

            return null;
        };
    }

    void execute() throws Exception {
        this.command.call();
    }

    private static String getValue(final String arg, final String resolvedValue, final Matcher matcher) {
        if (matcher.groupCount() == 2) {
            return arg.replace("{{"+matcher.group(1)+"}}", resolvedValue);
        } else {
            return resolvedValue;
        }
    }
}
