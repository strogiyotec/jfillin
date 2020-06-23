package jfill;

import mjson.Json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Cache {

    private final Json cache;

    public Cache(final String path) throws IOException {
        if (Files.exists(Paths.get(path))) {
            this.cache = Json.read(Files.readString(Path.of(path)));
        } else {
            this.cache = Json.make("{\"noTagValue:{}\"}");
        }
    }

    public List<String> pairHistory(TagGroup group) {
        if (this.cache.has(group.getTag())) {
            var values = new ArrayList<String>();
            var cache = this.cache.at(group.getTag()).at("values").asJsonList();
            for (final Json json : cache) {
                var groupValues = new ArrayList<String>();
                for (final String key : group) {
                    if (json.has(key)) {
                        groupValues.add(json.at(key).asString());
                    }
                }
                if (!groupValues.isEmpty()) {
                    values.add(String.join(", ", groupValues));
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
