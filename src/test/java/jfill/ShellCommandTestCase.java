package jfill;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

final class ShellCommandTestCase {

    @Test
    @DisplayName("Test that echo output was saved to file")
    void testOutput(@TempDir final Path tempDir) throws Exception {
        var cacheFile = tempDir.resolve("out.txt").toFile();
        new ShellCommand(
                new String[]{"echo", "{{msg1}}", "{{msg2}}"},
                new ValuesStorage(
                        Map.of(
                                Defaults.NO_TAG,
                                Map.of(
                                        "msg1", "Hello",
                                        "msg2", "World"
                                )
                        )
                ),
                new ProcessBuilder().redirectOutput(cacheFile)
        ).execute();
        Assertions.assertEquals(
                "Hello World",
                Files.readString(cacheFile.toPath()).replace("\n", "")
        );
    }

    @Test
    @DisplayName("Test that echo output with tags was saved to file")
    void testOutputWithTag(@TempDir final Path tempDir) throws Exception {
        var cacheFile = tempDir.resolve("out2.txt").toFile();
        new ShellCommand(
                new String[]{"echo", "{{tag1:msg1}}", "{{tag2:msg2}}"},
                new ValuesStorage(
                        Map.of(
                                "tag1",
                                Map.of(
                                        "msg1", "Hello"
                                ),
                                "tag2",
                                Map.of(
                                        "msg2", "World"
                                )
                        )
                ),
                new ProcessBuilder().redirectOutput(cacheFile)
        ).execute();
        Assertions.assertEquals(
                "Hello World",
                Files.readString(cacheFile.toPath()).replace("\n", "")
        );
    }

    @Test
    @DisplayName("Test that curl was executed and status is 200")
    void testExecuteCurl(@TempDir final Path tempDir) throws Exception {
        var cacheFile = tempDir.resolve("out4.txt").toFile();
        new ShellCommand(
                new String[]{"curl", "-I", "{{url}}.com"},
                new ValuesStorage(
                        Map.of(
                                Defaults.NO_TAG,
                                Map.of(
                                        "url", "https://www.test"
                                )
                        )
                ),
                new ProcessBuilder().redirectOutput(cacheFile)
        ).execute();
        Assertions.assertTrue(
                Files.readString(cacheFile.toPath()).contains("200 OK")
        );
    }

    @Test
    @DisplayName("Test that when value is part of the one word then only part of the word is replaced")
    void testValueIsPartOfWord(@TempDir final Path tempDir) throws Exception {
        var cacheFile = tempDir.resolve("out3.txt").toFile();
        new ShellCommand(
                new String[]{"echo", "{{url}}/users/1"},
                new ValuesStorage(
                        Map.of(
                                Defaults.NO_TAG,
                                Map.of(
                                        "url", "localhost"
                                )
                        )
                ),
                new ProcessBuilder().redirectOutput(cacheFile)
        ).execute();
        Assertions.assertEquals(
                "localhost/users/1",
                Files.readString(cacheFile.toPath()).replace("\n", "")
        );
    }
}
