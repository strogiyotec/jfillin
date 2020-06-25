package jfill;

import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.TerminalBuilder;

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
        new Main(args, Defaults.NO_TAG, Defaults.FILLIN_PTN).call();
    }

    @Override
    public Void call() throws Exception {
        System.out.print(
                new Command(
                        this.pattern,
                        this.args,
                        new Values(
                                new InputHandler(
                                        new LineReaderImpl(
                                                TerminalBuilder.terminal()
                                        )
                                ),
                                new Config(
                                        Defaults.CONFIG_PATH
                                )
                        ),
                        this.defaultTag
                )
        );

        //we need callable in order to throw exception
        return null;
    }
}
