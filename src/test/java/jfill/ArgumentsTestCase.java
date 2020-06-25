package jfill;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

final class ArgumentsTestCase {
    @Test
    void testNoTagArguments() {
        var arguments = new Arguments(new String[]{
                "echo",
                "{{hello}}",
                "{{world}}"

        }, Defaults.FILLIN_PTN);
        final List<Argument> list = fromIterable(arguments);
        Assertions.assertEquals(list.get(0).getKey(), "hello");
        Assertions.assertEquals(list.get(1).getKey(), "world");

        Assertions.assertFalse(list.get(0).hasTag());
        Assertions.assertFalse(list.get(1).hasTag());
    }

    @Test
    void testArgWithTag() {
        var arguments = new Arguments(new String[]{
                "echo",
                "{{psql:hello}}",
                "{{redis:world}}"

        }, Defaults.FILLIN_PTN);
        final List<Argument> list = fromIterable(arguments);
        Assertions.assertEquals(list.get(0).getKey(), "hello");
        Assertions.assertEquals(list.get(1).getKey(), "world");

        Assertions.assertEquals(list.get(0).getTag(), "psql");
        Assertions.assertEquals(list.get(1).getTag(), "redis");
    }
    private static List<Argument> fromIterable(final Iterable<Argument> iterable) {
        var list = new ArrayList<Argument>();
        iterable.forEach(list::add);
        return list;
    }
}
