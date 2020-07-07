package jfill;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;

import static jfill.Utils.configPath;

public final class ResolverWithTagTestCase {

    private static InputHandler handler;

    @BeforeAll
    static void initReader() {
        handler = Mockito.mock(InputHandler.class, Mockito.withSettings().stubOnly());
        //Default tag history
        Mockito.when(
                handler.getValue(
                        Mockito.eq(List.of("user", "port")),
                        Mockito.any()
                )
        ).thenReturn("postgres,5432");
    }

    @Test
    void test() throws IOException {
        var values = new ValuesResolver(handler, new Cache(configPath("cache_with_tag.json")));
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
