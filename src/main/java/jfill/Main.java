package jfill;

import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.TerminalBuilder;

import java.util.regex.Pattern;

public final class Main {

    private final String[] args;

    private final String defaultTag;

    private final Pattern pattern;

    private final Cache cache;

    public Main(final String[] args, final String defaultTag, final Pattern pattern, final Cache cache) {
        this.args = args;
        this.defaultTag = defaultTag;
        this.pattern = pattern;
        this.cache = cache;
    }

    public static void main(String[] args) throws Exception {
        new Main(
                args,
                Defaults.NO_TAG,
                Defaults.FILLIN_PTN,
                new Cache(Defaults.CACHE_PATH)
        ).execute();
    }

    private void printHelp() {
        System.out.println(
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
        System.out.println("jfillin 1.0");
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

    void execute() throws Exception {
        if (this.helpOrVersion()) {
            return;
        }
        final Values values = new Values(
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
        final ValuesStorage storage = values.resolve(
                new Arguments(
                        this.args,
                        this.pattern
                ),
                this.defaultTag
        );
        new ShellCommand(
                this.pattern,
                this.args,
                storage,
                this.defaultTag
        ).execute();
        //save new values in cache
        storage.flush(this.cache);
        this.cache.save();
    }
}
