package jfill;

import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

public final class Main implements Callable<Void> {

    private final String[] args;

    private final String defaultTag;

    private final Pattern pattern;

    public Main(final String[] args, final String defaultTag, final Pattern pattern) {
        this.args = args;
        this.defaultTag = defaultTag;
        this.pattern = pattern;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(System.getProperty("org.jline.terminal.dumb.color"));
        new Main(
                args,
                Defaults.NO_TAG,
                Defaults.FILLIN_PTN
        ).call();
    }

    private static void persistValues(
            final ValuesByTagStorage storage,
            final Config config
    ) throws IOException {
        storage.persist(config);
        config.save();
    }

    @Override
    public Void call() throws Exception {
        var config = new Config(Defaults.CONFIG_PATH);
        var storage = new ValuesByTagStorage();
        new ResolvedValues(
                new InputHandler(
                        new LineReaderImpl(
                                TerminalBuilder.builder().type("xterm").build()
                        )
                ),
                config
        ).resolve(
                new Arguments(
                        this.args,
                        this.pattern
                ),
                storage,
                this.defaultTag
        );
        new Command(
                this.pattern,
                this.args,
                storage,
                this.defaultTag
        ).call();
        persistValues(storage, config);
        //we need callable in order to throw exception
        return null;
    }
}
