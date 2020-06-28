package jfill;

import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.regex.Pattern;

public final class Main {

    private final String[] args;

    private final String defaultTag;

    private final Pattern pattern;

    public Main(final String[] args, final String defaultTag, final Pattern pattern) {
        this.args = args;
        this.defaultTag = defaultTag;
        this.pattern = pattern;
    }

    public static void main(String[] args) throws Exception {
        new Main(
                args,
                Defaults.NO_TAG,
                Defaults.FILLIN_PTN
        ).call();
    }


    private void call() throws Exception {
        var config = new Config(Defaults.CONFIG_PATH);
        final Values values = new Values(
                new InputHandler(
                        new LineReaderImpl(
                                TerminalBuilder.
                                        builder()
                                        .type("xterm")
                                        .build()
                        )
                ),
                config
        );
        final Storage storage = values.resolve(
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
        ).call();
        persistValues(storage, config);
    }

    private static void persistValues(
            final Storage storage,
            final Config config
    ) throws IOException {
        storage.persist(config);
        config.save();
    }
}
