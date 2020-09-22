package jfill;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

final class ArgumentsTestCase {
    @Test
    void testNoTagArguments() {
        var arguments = new Arguments(
            new String[]{
                "echo",
                "{{hello}}",
                "{{world}}"

            }
        );
        final List<Argument> list = fromIterable(arguments);
        Assertions.assertEquals(list.get(0).getKey(), "hello");
        Assertions.assertEquals(list.get(1).getKey(), "world");

        Assertions.assertFalse(list.get(0).hasTag());
        Assertions.assertFalse(list.get(1).hasTag());
    }

    private static List<Argument> fromIterable(final Iterable<Argument> iterable) {
        var list = new ArrayList<Argument>();
        iterable.forEach(list::add);
        return list;
    }

    @Test
    void testArgWithTag() {
        var arguments = new Arguments(new String[]{
            "echo",
            "{{psql:hello}}",
            "{{redis:world}}"

        });
        final List<Argument> list = fromIterable(arguments);
        Assertions.assertEquals(list.get(0).getKey(), "hello");
        Assertions.assertEquals(list.get(1).getKey(), "world");

        Assertions.assertEquals(list.get(0).getTag(), "psql");
        Assertions.assertEquals(list.get(1).getTag(), "redis");
    }
}
