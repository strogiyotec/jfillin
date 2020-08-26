package jfill;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

class Suggestions {

    private final List<String> suggestions;

    Suggestions(final List<String> suggestions) {
        this.suggestions = suggestions;
    }

    Suggestions(final List<Map<String, String>> history, final String delimiter) {
        this.suggestions = history.stream()
                .map(map -> String.join(delimiter, map.values()))
                .collect(Collectors.toList());
    }

    Suggestions(final List<Map<String, String>> history, Function<Map<String, String>, String> mapper) {
        this.suggestions = history.stream()
                .map(mapper)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Get list of suggestions.
     *
     * @return Suggestions
     */
    List<String> get() {
        return this.suggestions;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var plain = (Suggestions) o;
        return this.suggestions.equals(plain.get());
    }
}

