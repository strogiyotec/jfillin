package jfill;

import org.jline.reader.impl.LineReaderImpl;
import org.jline.reader.impl.completer.StringsCompleter;

import java.util.List;

final class InputHandler {

    private final LineReaderImpl reader;

    InputHandler(final LineReaderImpl reader) {
        this.reader = reader;
    }

    String getValue(final String word, final Suggestions suggestions) {
        this.reader.setCompleter(new StringsCompleter(suggestions.get()));
        return reader.readLine(String.format("%s: ", word));
    }

    String getValue(final List<String> words, final Suggestions suggestions) {
        this.reader.setCompleter(new StringsCompleter(suggestions.get()));
        var prompt = String.join(", ", words);
        return reader.readLine(String.format("%s: ", prompt));
    }
}

