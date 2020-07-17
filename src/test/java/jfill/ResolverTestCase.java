package jfill;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

final class ResolverTestCase {

    private static InputHandler handler;

    @BeforeAll
    static void initReader() {
        handler = new MockedInputHandler(
                Map.of(
                        "surname", "abdrazak",
                        "name", "almas",
                        "email", "almas337519@gmail.com"
                )
        );
    }

    @Test
    void testValueFromHistory() throws IOException {
        var values = new UsingTerminalInput(handler, new Cache(Utils.configPath("cache_with_default_tag.json")));
        var storage = values.resolve(
                new Arguments(
                        new String[]{
                                "echo",
                                "{{name}}",
                                "{{surname}}",
                        }
                ));
        Assertions.assertAll(
                () -> Assertions.assertEquals(storage.get(Defaults.NO_TAG, "name"), "almas"),
                () -> Assertions.assertEquals(storage.get(Defaults.NO_TAG, "surname"), "abdrazak")
        );
    }

    @Test
    void testValueWithoutHistory() throws IOException {
        var values = new UsingTerminalInput(handler, new Cache(Utils.configPath("cache_with_default_tag.json")));
        var storage = values.resolve(new Arguments(
                new String[]{
                        "echo",
                        "{{email}}"
                }
        ));
        Assertions.assertEquals(storage.get(Defaults.NO_TAG, "email"), "almas337519@gmail.com");
    }
}
