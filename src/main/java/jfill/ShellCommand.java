package jfill;

import java.util.ArrayList;
import java.util.concurrent.Callable;

final class ShellCommand {

    private final Callable<Void> command;

    ShellCommand(
            final String[] args,
            final ResolvedValues storage,
            final ProcessBuilder builder
    ) {
        this.command = () -> {
            var resolvedArgs = new ArrayList<String>(args.length);
            for (var arg : args) {
                var matcher = Defaults.FILLIN_PTN.matcher(arg);
                if (matcher.find()) {
                    var key = matcher.group(2);
                    var split = key.split(":");
                    //doesn't have tag
                    if (split.length == 1) {
                        resolvedArgs.add(arg.replaceAll("\\{\\{(.*)}}", storage.getValueByTag(Defaults.NO_TAG, key)));
                    } else {
                        resolvedArgs.add(arg.replaceAll("\\{\\{(.*)}}", storage.getValueByTag(split[0], split[1])));
                    }
                } else {
                    resolvedArgs.add(arg);
                }
            }
            var process = builder
                    .command(resolvedArgs)
                    .start();
            process.waitFor();

            //because callable has to return something
            return null;
        };
    }

    void execute() throws Exception {
        this.command.call();
    }
}
