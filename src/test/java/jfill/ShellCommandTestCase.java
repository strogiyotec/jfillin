package jfill;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

final class ShellCommandTestCase {

    @Test
    void testShellCommand() {
        Assertions.assertEquals(
                "psql -h localhost -p 123 -u postgres",
                new ShellCommand().join(
                        new String[]{"psql", "-h", "{{host}}", "-p", "{{port}}", "-u", "{{user}}"},
                        Main.FILLIN_PTN,
                        new Scanner("localhost 123 postgres"),
                        new PrintStream(
                                new ByteArrayOutputStream(100)
                        )
                )
        );
    }
}
