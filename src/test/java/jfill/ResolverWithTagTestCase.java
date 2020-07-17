package jfill;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static jfill.Utils.configPath;

final class ResolverWithTagTestCase {

    private static InputHandler handler;

    @BeforeAll
    static void initReader() {
        handler = new MockedInputHandler(
                Map.of(
                        "user", "postgres",
                        "port", "5432"
                )
        );
    }

    @Test
    void testResolverWithTag() throws IOException {
        var values = new UsingTerminalInput(handler, new Cache(configPath("cache_with_tag.json")));
        var storage = values.resolve(new Arguments(
                new String[]{
                        "echo",
                        "{{psql:user}}",
                        "{{psql:port}}"
                }
        ));
        Assertions.assertAll(
                () -> Assertions.assertEquals(storage.get("psql", "user"), "postgres"),
                () -> Assertions.assertEquals(storage.get("psql", "port"), "5432")
        );
    }
}
