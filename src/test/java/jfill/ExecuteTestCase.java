package jfill;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

final class ExecuteTestCase {

    @Test
    void testVersion() throws Exception {
        try (var stream = new ByteArrayOutputStream()) {
            try (var print = new PrintStream(stream)) {
                new Execution(new String[]{"-v"}, new Cache(Utils.configPath("plain_cache.json")), new ProcessBuilder(), print).execute();
                Assertions.assertTrue(stream.toString().contains("jfillin 1.0"));
            }
        }
    }
}
