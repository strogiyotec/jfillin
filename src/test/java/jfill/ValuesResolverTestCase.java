package jfill;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

final class ValuesResolverTestCase {

    private static InputHandler handler;

    @BeforeAll
    static void initReader() {
        handler = Mockito.mock(InputHandler.class);
        //Default tag history
        Mockito.when(
                handler.getValue(
                        Mockito.eq("surname"),
                        Mockito.eq(new Suggestions.Plain(List.of("abdrazak")))
                )
        ).thenReturn("abdrazak");
        Mockito.when(
                handler.getValue(
                        Mockito.eq("name"),
                        Mockito.eq(new Suggestions.Plain(List.of("almas")))
                )
        ).thenReturn("almas");
        //Values without history
        Mockito.when(
                handler.getValue(
                        Mockito.eq("email"),
                        Mockito.eq(new Suggestions.Plain(Collections.emptyList()))
                )
        ).thenReturn("almas337519@gmail.com");
    }

    @Test
    void testResolveDefaultTag() throws IOException {
        var values = new ValuesResolver(handler, new Cache(Utils.configPath("cache_with_default_tag.json")));
        var storage = values.resolve(new Arguments(
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
    void testDefaultTagWithoutHistory() throws IOException {
        var values = new ValuesResolver(handler, new Cache(Utils.configPath("cache_with_default_tag.json")));
        var storage = values.resolve(new Arguments(
                new String[]{
                        "echo",
                        "{{email}}"
                }
        ));
        Assertions.assertEquals(storage.get(Defaults.NO_TAG, "email"), "almas337519@gmail.com");
    }
}
