package jfill;

import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.TerminalBuilder;

public final class Main {

    /**
     * Utility class.
     */
    private Main() {

    }

    public static void main(final String[] args) throws Exception {
        var cache = new Cache(Defaults.CACHE_PATH);
        new Execution(
                args,
                cache,
                new ProcessBuilder().inheritIO(),
                System.out,
                new ValuesResolver(
                        new InputHandler(
                                new LineReaderImpl(
                                        TerminalBuilder.
                                                builder()
                                                .type("xterm")
                                                .build()
                                )
                        ),
                        cache
                )
        ).execute();
    }
}
