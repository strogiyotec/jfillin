package jfill;

import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.impl.DumbTerminal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

final class TerminalInputTestCase {

    @Test
    void testListInput() throws IOException {
        try (var terminal = new DumbTerminal(System.in, System.out)) {
            var terminalInput = new TerminalInput(
                    new MockedLineReader(
                            terminal,
                            Map.of("name: ", "Almas")
                    )
            );
            Assertions.assertEquals(
                    terminalInput.getValue(
                            List.of("name"),
                            new Suggestions(Collections.emptySet())
                    ),
                    "Almas"
            );
        }
    }

    @Test
    void testSingleInput() throws IOException {
        try (var terminal = new DumbTerminal(System.in, System.out)) {
            var terminalInput = new TerminalInput(
                    new MockedLineReader(
                            terminal,
                            Map.of("name: ", "Almas")
                    )
            );
            Assertions.assertEquals(
                    terminalInput.getValue(
                            "name",
                            new Suggestions(Collections.emptySet())
                    ),
                    "Almas"
            );
        }
    }
}
