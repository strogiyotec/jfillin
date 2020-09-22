package jfill;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

class Suggestions {

    private final Set<String> suggestions;

    Suggestions(final Set<String> suggestions) {
        this.suggestions = suggestions;
    }

    Suggestions(final List<Map<String, String>> history, final String delimiter) {
        this.suggestions = history.stream()
            .map(map -> String.join(delimiter, map.values()))
            .collect(Collectors.toSet());
    }

    Suggestions(final List<Map<String, String>> history, Function<Map<String, String>, String> mapper) {
        this.suggestions = history.stream()
            .map(mapper)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var plain = (Suggestions) o;
        return this.suggestions.equals(plain.get());
    }

    /**
     * Get list of suggestions.
     * @return Suggestions
     */
    Set<String> get() {
        return this.suggestions;
    }
}

