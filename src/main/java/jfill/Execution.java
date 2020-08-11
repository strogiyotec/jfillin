package jfill;

import org.jline.reader.UserInterruptException;

import java.io.PrintStream;

final class Execution {

    private final String[] args;

    private final Cache cache;

    private final ProcessBuilder builder;

    private final PrintStream output;

    private final ValuesResolver resolver;

    Execution(
            final String[] args,
            final Cache cache,
            final ProcessBuilder builder,
            final PrintStream output,
            final ValuesResolver resolver
    ) {
        this.args = args;
        this.cache = cache;
        this.builder = builder;
        this.output = output;
        this.resolver = resolver;
    }

    /**
     * Execute jfill.
     *
     * @throws Exception If failed
     */
    void execute() throws Exception {
        if (!this.helpOrVersion()) {
            try {
                var resolvedValues = this.resolver.resolve(new Arguments(this.args));
                new ShellCommand(
                        this.args,
                        resolvedValues,
                        this.builder
                ).execute();
                //save new valuesResolver in cache
                resolvedValues.flush(this.cache);
            } catch (final UserInterruptException exc) {
                //Do nothing
            }
        }
    }

    private void printHelp() {
        this.output.println(
                String.join(
                        "\n",
                        "NAME:",
                        "\tjfillin- fill your command and execute",
                        "USAGE:",
                        "\tjfill echo {{message}}",
                        "\tjfill psql -h {{psql:hostname}} -U {{psql:username}} -d {{psql:dbname}}",
                        "VERSION:",
                        "\t2.0",
                        "AUTHOR:",
                        "\talmas337519@gmail.com"
                )
        );
    }

    private void printVersion() {
        this.output.println(Defaults.VERSION);
    }

    /**
     * Check if jfill should print help or version.
     * If so then print it
     *
     * @return If help or version was printed
     */
    private boolean helpOrVersion() {
        if (this.args.length == 0) {
            this.printHelp();
            return true;
        }
        if (this.args.length == 1) {
            if (this.args[0].equals("-v") || this.args[0].equals("-version") || this.args[0].equals("--v") || this.args[0].equals("--version")) {
                this.printVersion();
                return true;
            } else if (this.args[0].equals("-h") || this.args[0].equals("-help") || this.args[0].equals("--h") || this.args[0].equals("--help")) {
                this.printHelp();
                return true;
            }
        }
        return false;
    }

}
