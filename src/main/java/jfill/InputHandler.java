package jfill;

import org.jline.reader.impl.LineReaderImpl;
import org.jline.reader.impl.completer.StringsCompleter;

import java.util.List;

public final class InputHandler {

    private final LineReaderImpl reader;

    public InputHandler(final LineReaderImpl reader) {
        this.reader = reader;
    }

    public String getValue(final String word, final List<String> suggestions) {
        this.reader.setCompleter(new StringsCompleter(suggestions));
        return reader.readLine(String.format("%s: ", word));
    }
}

