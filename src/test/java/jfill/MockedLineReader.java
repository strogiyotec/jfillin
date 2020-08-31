package jfill;

import org.jline.reader.EndOfFileException;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.Terminal;

import java.io.IOException;
import java.util.Map;

final class MockedLineReader extends LineReaderImpl {

    private final Map<String, String> predefinedValues;

    MockedLineReader(final Terminal terminal, final Map<String, String> predefined) throws IOException {
        super(terminal);
        this.predefinedValues = predefined;
    }

    @Override
    public String readLine(final String prompt) throws UserInterruptException, EndOfFileException {
        return this.predefinedValues.get(prompt);
    }
}
