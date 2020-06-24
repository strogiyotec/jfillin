package jfill;

import mjson.Json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public final class Config {

    private final Json cache;

    public Config(final String path) throws IOException {
        if (Files.exists(Paths.get(path))) {
            this.cache = Json.read(Files.readString(Path.of(path)));
        } else {
            this.cache = Json.make("{\"noTagValue:{}\"}");
        }
    }

    public List<String> history(final String word, final String tag) {
        if (this.cache.has(tag)) {
            final List<Json> cache = this.cache.at(tag).at("values").asJsonList();
            return cache.stream()
                    .filter(json -> json.has(word))
                    .map(json -> json.at(word).asString())
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public List<Map<String, String>> historyPerGroup(final TagGroup group) {
        if (this.cache.has(group.getTag())) {
            var values = new ArrayList<Map<String, String>>();
            var cache = this.cache.at(group.getTag()).at("values").asJsonList();
            for (var json : cache) {
                var groupValues = new HashMap<String, String>();
                for (final String key : group.getKeys()) {
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

    void save(final Json json) {
        System.out.println(json.asString());
    }
}
