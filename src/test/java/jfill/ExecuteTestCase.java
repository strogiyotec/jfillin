package jfill;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import mjson.Json;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

final class ExecuteTestCase {

    @Test
    void testVersion() throws Exception {
        try (var stream = new ByteArrayOutputStream()) {
            try (var print = new PrintStream(stream)) {
                new Execution(
                    new String[]{"-v"},
                    new Cache(Utils.testConfigPath("plain_cache.json")),
                    new ProcessBuilder(),
                    print,
                    new ResolveFromTerminal(
                        new MockedInputHandler(Collections.emptyMap()),
                        new Cache()
                    )
                ).execute();
                Assertions.assertTrue(stream.toString().contains(Defaults.VERSION));
            }
        }
    }

    @Test
    @DisplayName("Test that help info is displayed")
    void testHelp() throws Exception {
        try (var stream = new ByteArrayOutputStream()) {
            try (var print = new PrintStream(stream)) {
                new Execution(
                    new String[]{"-h"},
                    new Cache(Utils.testConfigPath("plain_cache.json")),
                    new ProcessBuilder(),
                    print,
                    new ResolveFromTerminal(
                        new MockedInputHandler(Collections.emptyMap()),
                        new Cache()
                    )
                ).execute();
                Assertions.assertTrue(stream.toString().contains(Defaults.VERSION));
                Assertions.assertTrue(stream.toString().contains("almas337519@gmail.com"));
                Assertions.assertTrue(stream.toString().contains("USAGE"));

            }
        }
    }

    @Test
    @DisplayName("Test that execution saves new values in the cache file")
    void testCachePersistence(@TempDir final Path tempDir) throws Exception {
        var outputFile = tempDir.resolve("output.txt");
        var mock = new MockedValuesResolver(
            new ResolvedValues(
                Map.of(
                    Defaults.NO_TAG,
                    Map.of("msg", "Hello")
                )
            )
        );
        var cacheFile = tempDir.resolve("test.json");
        Files.write(cacheFile, "{}".getBytes());
        try (var outputStorage = new ByteArrayOutputStream()) {
            try (var stream = new PrintStream(outputStorage)) {
                new Execution(
                    new String[]{"echo", "{{msg}}"},
                    new Cache(cacheFile.toFile()),
                    new ProcessBuilder().redirectOutput(outputFile.toFile()),
                    stream,
                    mock
                ).execute();

                //msg was saved in file
                Assertions.assertEquals(
                    Json.read(Files.readString(cacheFile))
                        .at(Defaults.NO_TAG)
                        .at("values")
                        .asJsonList()
                        .get(0)
                        .at("msg")
                        .asString(),
                    "Hello"
                );
            }
        }
    }
}
