package jfill;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores resolved values.
 */
final class ResolvedValuesStorage {

    /**
     * Storage.
     * Key is tag
     * Value is map where key is variable name and value is a variable's value
     */
    private final Map<String, Map<String, String>> storage;

    ResolvedValuesStorage() {
        this.storage = new HashMap<>();
    }

    ResolvedValuesStorage(final Map<String, Map<String, String>> storage) {
        this.storage = storage;
    }

    void addTagIfAbsent(final String tag) {
        this.storage.computeIfAbsent(tag, s -> new HashMap<>());
    }

    boolean tagHasKey(final String tag, final String key) {
        return this.storage.get(tag).containsKey(key);
    }

    void store(final String tag, final String key, final String value) {
        this.storage.get(tag).put(key, value.trim());
    }

    String get(final String tag, final String key) {
        return this.storage.get(tag).get(key);
    }

    /**
     * Flush all entries to cache.
     *
     * @param cache Cache
     */
    void flush(final Cache cache) {
        this.storage.forEach(cache::addEntry);
    }
}
