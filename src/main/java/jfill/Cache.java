package jfill;

import mjson.Json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

final class Cache {

    private final Json cache;

    private final String path;

    Cache(final String path) throws IOException {
        if (Files.exists(Paths.get(path))) {
            this.cache = Json.read(Files.readString(Path.of(path)));
        } else {
            this.cache = Json.object().set("noTag", Json.object().set("values", Json.array()));
        }
        this.path = path;
    }

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

    List<String> history(final String word, final String tag) {
        if (this.cache.has(tag)) {
            var cache = this.cache.at(tag).at("values").asJsonList();
            return cache.stream()
                    .filter(json -> json.has(word))
                    .map(json -> json.at(word).asString())
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

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

    void save() throws IOException {
        Files.write(Paths.get(this.path), this.cache.toString().getBytes());
    }
}
