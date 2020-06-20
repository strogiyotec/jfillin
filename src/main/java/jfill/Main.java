package jfill;

import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.regex.Pattern;

public final class Main {
    public static final Pattern FILLIN_PTN = Pattern.compile("\\{\\{(.*)}}");

    public static void main(String[] args) throws IOException {
        System.out.println(
                new ShellCommand(
                        new InputHandler(
                                new LineReaderImpl(
                                        TerminalBuilder.terminal()
                                )
                        ),
                        new Cache(Config.CONFIG_PATH)
                ).join(args, FILLIN_PTN)
        );
    }
}
