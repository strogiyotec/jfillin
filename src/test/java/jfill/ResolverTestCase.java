package jfill;

import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
        var values = new ResolveFromTerminal(handler, new Cache(Utils.testConfigPath("cache_with_default_tag.json")));
        var storage = values.resolve(
            new Arguments(
                new String[]{
                    "echo",
                    "{{name}}",
                    "{{surname}}",
                }
            ));
        Assertions.assertAll(
            () -> Assertions.assertEquals(storage.getValueByTag(Defaults.NO_TAG, "name"), "almas"),
            () -> Assertions.assertEquals(storage.getValueByTag(Defaults.NO_TAG, "surname"), "abdrazak")
        );
    }

    @Test
    void testValueWithoutHistory() throws IOException {
        var values = new ResolveFromTerminal(handler, new Cache(Utils.testConfigPath("cache_with_default_tag.json")));
        var storage = values.resolve(new Arguments(
            new String[]{
                "echo",
                "{{email}}"
            }
        ));
        Assertions.assertEquals(storage.getValueByTag(Defaults.NO_TAG, "email"), "almas337519@gmail.com");
    }
}
