package jfill;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores resolved values.
 */
final class ResolvedValues {

    /**
     * Storage.
     * Key is tag
     * Value is map where key is variable name and value is a variable's value
     */
    private final Map<String, Map<String, String>> storage;

    ResolvedValues() {
        this.storage = new HashMap<>();
    }

    ResolvedValues(final Map<String, Map<String, String>> storage) {
        this.storage = storage;
    }

    void addTagIfAbsent(final String tag) {
        this.storage.computeIfAbsent(tag, s -> new HashMap<>());
    }

    boolean tagHasKey(final String tag, final String key) {
        return this.storage.get(tag).containsKey(key);
    }

    /**
     * Store resolved values in memory.
     *
     * @param tag   Tag of the resolved value
     * @param key   Name of the resolve value
     * @param value Resolved value
     */
    void addResolvedValue(final String tag, final String key, final String value) {
        this.storage.get(tag).put(key, value.trim());
    }

    /**
     * Get resolved value by it's name and tag.
     * @param tag Tag of resolved value
     * @param key Name of the resolved value
     * @return Resolved value
     */
    String getValueByTag(final String tag, final String key) {
        return this.storage.get(tag).get(key);
    }

    /**
     * Flush all entries to cache.
     *
     * @param cache Cache
     */
    void flush(final Cache cache) throws IOException {
        this.storage.forEach(cache::addEntry);
        cache.save();
    }
}
