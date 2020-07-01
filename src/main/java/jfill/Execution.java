package jfill;

import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.TerminalBuilder;

import java.io.PrintStream;

final class Execution {

    private final String[] args;

    private final Cache cache;

    private final ProcessBuilder builder;

    private final PrintStream output;

    Execution(final String[] args, final Cache cache, final ProcessBuilder builder, final PrintStream output) {
        this.args = args;
        this.cache = cache;
        this.builder = builder;
        this.output = output;
    }

    /**
     * Execute jfill.
     *
     * @throws Exception If failed
     */
    void execute() throws Exception {
        if (this.helpOrVersion()) {
            return;
        }
        final ValuesResolver valuesResolver = new ValuesResolver(
                new InputHandler(
                        new LineReaderImpl(
                                TerminalBuilder.
                                        builder()
                                        .type("xterm")
                                        .build()
                        )
                ),
                this.cache
        );
        final ValuesStorage storage = valuesResolver.resolve(new Arguments(this.args));
        new ShellCommand(
                Defaults.FILLIN_PTN,
                this.args,
                storage,
                Defaults.NO_TAG,
                this.builder
        ).execute();
        //save new valuesResolver in cache
        storage.flush(this.cache);
        this.cache.save();
    }

    private void printHelp() {
        this.output.println(
                String.join(
                        "\n",
                        "NAME:",
                        "\tjfillin- fill your command and execute",
                        "VERSION:",
                        "\t1.0",
                        "AUTHOR:",
                        "\talmas337519@gmail.com"
                )
        );
    }

    private void printVersion() {
        this.output.println("jfillin 1.0");
    }

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
