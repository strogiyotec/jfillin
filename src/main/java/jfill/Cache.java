package jfill;

import mjson.Json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Cache {

    private final String path;

    public Cache(final String path) {
        this.path = path;
    }

    Json read() throws IOException {
        if (Files.exists(Paths.get(path))) {
            return Json.read(Files.readString(Path.of(path)));
        } else {
            return Json.make("{\"noTagValue:{}\"}");
        }
    }

    void save(final Json json) {
        System.out.println(json.asString());
    }
}
