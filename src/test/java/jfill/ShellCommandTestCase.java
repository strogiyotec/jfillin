package jfill;

import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.TerminalBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

final class ShellCommandTestCase {

    @Test
    void testShellCommand() throws IOException {
        Assertions.assertEquals(
                "psql -h localhost -p 123 -u postgres",
                new ShellCommand().join(
                        new String[]{"psql", "-h", "{{host}}", "-p", "{{port}}", "-u", "{{user}}"},
                        Main.FILLIN_PTN,
                        new LineReaderImpl(TerminalBuilder.terminal()),
                        new PrintStream(
                                new ByteArrayOutputStream(100)
                        )
                )
        );
    }
}
