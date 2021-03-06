package jfill;

import java.util.List;
import org.jline.reader.impl.LineReaderImpl;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.StringsCompleter;

final class TerminalInput implements InputHandler {

    private final LineReaderImpl reader;

    TerminalInput(final LineReaderImpl reader) {
        this.reader = reader;
    }

    @Override
    public String getValue(final List<String> words, final Suggestions suggestions) {
        this.reader.setCompleter(new StringsCompleter(suggestions.get()));
        var prompt = String.join(", ", words);
        return this.reader.readLine(String.format("%s: ", prompt));
    }

    @Override
    public String getValue(final String word, final Suggestions suggestions) {
        this.reader.setCompleter(
            new ArgumentCompleter(
                new StringsCompleter(
                    suggestions.get()
                ),
                NullCompleter.INSTANCE
            )
        );
        return this.reader.readLine(String.format("%s: ", word));
    }
}

