package jfill;

import org.jline.reader.impl.LineReaderImpl;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.StringsCompleter;

import java.util.List;

final class TerminalInput implements InputHandler{

    private final LineReaderImpl reader;

    TerminalInput(final LineReaderImpl reader) {
        this.reader = reader;
    }

    public String getValue(final String word, final Suggestions suggestions) {
        this.reader.setCompleter(
                new ArgumentCompleter(
                        new StringsCompleter(
                                suggestions.get()
                        ),
                        NullCompleter.INSTANCE
                )
        );
        return reader.readLine(String.format("%s: ", word));
    }

    public String getValue(final List<String> words, final Suggestions suggestions) {
        this.reader.setCompleter(new StringsCompleter(suggestions.get()));
        var prompt = String.join(", ", words);
        return reader.readLine(String.format("%s: ", prompt));
    }
}

