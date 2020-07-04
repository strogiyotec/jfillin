package jfill;

import mjson.Json;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * History cache saved in json file.
 */
final class Cache {

    private final Json cache;

    private final String path;

    Cache(final File file) throws IOException {
        if (file.exists()) {
            this.cache = Json.read(Files.readString(file.toPath()));
        } else {
            this.cache = Json.object().set("noTag", Json.object().set("values", Json.array()));
        }
        this.path = file.getAbsolutePath();
    }

    Cache(final String path) throws IOException {
        if (Files.exists(Paths.get(path))) {
            this.cache = Json.read(Files.readString(Path.of(path)));
        } else {
            this.cache = Json.object().set("noTag", Json.object().set("values", Json.array()));
        }
        this.path = path;
    }

    /**
     * Add new entry to cache.
     * Saves this entry in memory.
     *
     * @param tag   Tag
     * @param entry Entry to save
     * @return True if entry was saved in memory
     */
    boolean addEntry(final String tag, final Map<String, String> entry) {
        if (entry.isEmpty()) {
            return false;
        }
        if (this.cache.has(tag)) {
            var list = this.cache.at(tag).at("values").asJsonList();
            final boolean has = list.stream().anyMatch(json -> json.asMap().entrySet().containsAll(entry.entrySet()));
            if (!has) {
                this.cache.at(tag).at("values").add(Json.make(entry));
                return true;
            }
        } else {
            this.cache.set(tag, Json.object().set("values", Json.array()));
            this.cache.at(tag).at("values").add(Json.make(entry));
            return true;
        }
        return false;
    }

    /**
     * Given group has only one key.
     * Get history for given tag and single key
     * with the same key are chosen
     *
     * @param group Group
     * @return Cache for given group
     */
    List<String> history(final TagGroup group) {
        if (this.cache.has(group.getTag())) {
            var cache = this.cache.at(group.getTag()).at("values").asJsonList();
            return cache.stream()
                    .filter(json -> json.has(group.getKeys().get(0)))
                    .map(json -> json.at(group.getKeys().get(0)).asString())
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Fetch history by given group.
     * All cache entries with tag from given
     * group will be selected
     *
     * @param group Group
     * @return History
     */
    List<Map<String, String>> historyPerGroup(final TagGroup group) {
        if (this.cache.has(group.getTag())) {
            var values = new ArrayList<Map<String, String>>();
            var cache = this.cache.at(group.getTag()).at("values").asJsonList();
            for (var json : cache) {
                var groupValues = new HashMap<String, String>();
                for (var key : group.getKeys()) {
                    if (json.has(key)) {
                        groupValues.put(key, json.at(key).asString());
                    }
                }
                if (!groupValues.isEmpty()) {
                    values.add(groupValues);
                }
            }
            return values;
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Persist cache to file.
     *
     * @throws IOException If failed
     */
    void save() throws IOException {
        Files.write(Paths.get(this.path), this.cache.toString().getBytes());
    }
}
