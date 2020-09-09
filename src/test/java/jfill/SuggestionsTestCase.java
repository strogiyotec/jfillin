package jfill;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

final class SuggestionsTestCase {

    @Test
    @DisplayName("Remove duplicates")
    void testDuplicates() {
        var suggestions = new Suggestions(
                List.of(
                        Map.of("key", "value"),
                        Map.of("key", "value")
                ),
                ","
        );
        Assertions.assertEquals(
                suggestions.get(),
                Set.of("value")
        );
    }
}
