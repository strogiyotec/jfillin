package jfill;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public interface Suggestions {

    /**
     * Get list of suggestions.
     *
     * @return Suggestions
     */
    List<String> get();

    /**
     * Create suggestions from given list.
     */
    final class Plain implements Suggestions {
        private final List<String> suggestions;

        Plain(final List<String> history) {
            this.suggestions = history;
        }

        @Override
        public List<String> get() {
            return this.suggestions;
        }
    }

    /**
     * Create suggestions by joining values from history.
     */
    final class JoinedHistory implements Suggestions {
        private final List<String> suggestions;

        JoinedHistory(final List<Map<String, String>> history) {
            this.suggestions = history.stream()
                    .map(map -> String.join(",", map.values()))
                    .collect(Collectors.toList());
        }

        @Override
        public List<String> get() {
            return this.suggestions;
        }
    }

    /**
     * Create suggestions by selecting values by specified key.
     */
    final class ByKey implements Suggestions {

        private final List<String> suggestions;

        ByKey(final List<Map<String, String>> history, final String key) {
            this.suggestions = history.stream()
                    .map(map -> map.get(key))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        @Override
        public List<String> get() {
            return this.suggestions;
        }
    }


}
