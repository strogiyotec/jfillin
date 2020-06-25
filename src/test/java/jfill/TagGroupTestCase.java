package jfill;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

final class TagGroupTestCase {

    @Test
    void testGroup() {
        var group = new TagGroup(
                new Arguments(
                        new String[]{
                                "psql",
                                "-p {{psql:port}}",
                                "-h {{psql:host}}",
                        },
                        Defaults.FILLIN_PTN),
                "psql"
        );
        Assertions.assertEquals(group.getTag(), "psql");
        Assertions.assertEquals(group.getKeys().size(), 2);

        Assertions.assertEquals(group.getKeys().get(0), "port");
        Assertions.assertEquals(group.getKeys().get(1), "host");
    }
}
