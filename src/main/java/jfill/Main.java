package jfill;

import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

public final class Main {
    public static final Pattern FILLIN_PTN = Pattern.compile("\\{\\{(.*)}}");

    public static void main(String[] args) throws IOException {
        final Values shellCommand = new Values(
                new InputHandler(
                        new LineReaderImpl(
                                TerminalBuilder.terminal()
                        )
                ),
                new Config(UTIL.CONFIG_PATH)
        );
        final Map<String, Map<String, String>> resolvedValues = shellCommand.resolve(new Arguments(args, FILLIN_PTN), "noTag");
        final String noTag = new Command().fill(FILLIN_PTN, args, resolvedValues, "noTag");
        System.out.println(noTag);
    }
}
