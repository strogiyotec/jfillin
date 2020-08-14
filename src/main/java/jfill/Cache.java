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
 * History inMemoryCache saved in json file.
 */
final class Cache {

    private final Json inMemoryCache;

    private final String path;

    /**
     * Ctor.
     * Read given json file and stores all entries
     * in inMemoryCache
     * If json file doesn't exist then create it
     *
     * @param file Json file
     * @throws IOException If failed
     */
    Cache(final File file) throws IOException {
        if (file.exists()) {
            this.inMemoryCache = Json.read(Files.readString(file.toPath()));
        } else {
            this.inMemoryCache = Json.object().set("noTag", Json.object().set("values", Json.array()));
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
        this.path = file.getAbsolutePath();
    }

    /**
     * Ctor.
     * Creates empty inMemoryCache
     */
    Cache() {
        this.inMemoryCache = Json.nil();
        this.path = null;
    }

    Cache(final String path) throws IOException {
        if (Files.exists(Paths.get(path))) {
            this.inMemoryCache = Json.read(Files.readString(Path.of(path)));
        } else {
            this.inMemoryCache = Json.object().set("noTag", Json.object().set("values", Json.array()));
        }
        this.path = path;
    }

    /**
     * Add new entry to inMemoryCache.
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
        if (this.inMemoryCache.has(tag)) {
            var list = this.inMemoryCache.at(tag).at("values").asJsonList();
            //if given entry doesn't exist in json file
            var has = list.stream().anyMatch(json -> json.asMap().entrySet().containsAll(entry.entrySet()));
            if (!has) {
                this.inMemoryCache.at(tag).at("values").add(Json.make(entry));
                return true;
            }
        } else {
            //create new tag with given entry
            this.inMemoryCache.set(tag, Json.object().set("values", Json.array()));
            this.inMemoryCache.at(tag).at("values").add(Json.make(entry));
            return true;
        }
        return false;
    }

    /**
     * Get history for key with given tag.
     *
     * @param key Key
     * @param tag Tag
     * @return Cache for given group
     */
    List<String> historyPerKey(final String tag, final String key) {
        if (this.inMemoryCache.has(tag)) {
            var cache = this.inMemoryCache.at(tag).at("values").asJsonList();
            return cache.stream()
                    .filter(json -> json.has(key))
                    .map(json -> json.at(key).asString())
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Fetch history by given group.
     * All inMemoryCache entries with tag from given
     * group will be selected
     *
     * @param group Group
     * @return History
     */
    List<Map<String, String>> historyPerGroup(final TagGroup group) {
        if (this.inMemoryCache.has(group.getTag())) {
            var values = new ArrayList<Map<String, String>>();
            var cache = this.inMemoryCache.at(group.getTag()).at("values").asJsonList();
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
     * Persist inMemoryCache to file.
     *
     * @throws IOException If failed
     */
    void save() throws IOException {
        Files.write(Paths.get(this.path), this.inMemoryCache.toString().getBytes());
    }
}
