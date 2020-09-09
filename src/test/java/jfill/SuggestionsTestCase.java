package jfill;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public final class SuggestionsTestCase {


    @Test
    void testDuplicates() {
        var suggestions = new Suggestions(
                List.of(
                        Map.of("key", "value"),
                        Map.of("key", "value")
                ),
                ","
        );
        System.out.println(suggestions.get());
    }
}
