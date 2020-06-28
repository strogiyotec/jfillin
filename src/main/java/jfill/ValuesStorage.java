package jfill;

import java.util.HashMap;
import java.util.Map;

final class ValuesStorage {

    /**
     * Storage.
     * Key is tag
     * Value is map of argument-value
     */
    private final Map<String, Map<String, String>> storage;

    ValuesStorage() {
        this.storage = new HashMap<>();
    }

    void addTag(final String tag) {
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
