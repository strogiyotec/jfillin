package jfill;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class CacheTestCase {

    private static Cache cache;

    @BeforeAll
    @DisplayName("Init cache")
    static void initConfig() throws IOException {
        cache = new Cache(Utils.testConfigPath("plain_cache.json"));
    }

    /**
     * Create file that doesn't exist.
     *
     * @param tempPath Temp path
     * @throws IOException If failed
     * @see <a href="https://github.com/strogiyotec/jfillin/issues/11">Github Issue</a>
     */
    @Test
    @DisplayName("Cache will create new file if it doesn't exist")
    void testFileCreated(@TempDir final Path tempPath) throws IOException {
        //non existing file in non existing directory
        final Path testPath = tempPath.resolve("tempdir/test_file.json");
        Assertions.assertFalse(Files.exists(testPath));
        //ctor will create new file if it doesn't exist
        new Cache(testPath.toFile());
        Assertions.assertTrue(Files.exists(testPath));
    }

    @Test
    @DisplayName("Test that cache has two different values for given word")
    void testHistoryForWord() {
        Assertions.assertEquals(
                cache.historyPerKey("psql", "user"),
                Set.of("postgres", "admin")
        );

    }

    @Test
    @DisplayName("Test that cache doesn't exist for given word")
    void testHistoryIsEmpty() {
        Assertions.assertEquals(
                cache.historyPerKey("psql", "connections"),
                Collections.emptySet()
        );
    }

    @Test
    @DisplayName("Test that both history entries were fetched for given tag")
    void testGroupHistory() {
        var tag = "psql";
        var history = cache.historyPerGroup(
                new TagGroup(
                        new Arguments(
                                new String[]{
                                        tag,
                                        "-host {{psql:host}}",
                                        "-port {{psql:port}}",
                                }
                        ),
                        tag
                ));
        Assertions.assertEquals(history.size(), 2);

        Assertions.assertAll(
                () -> Assertions.assertEquals(history.get(0).get("host"), "localhost"),
                () -> Assertions.assertEquals(history.get(0).get("port"), "5432")
        );
        Assertions.assertAll(
                () -> Assertions.assertEquals(history.get(1).get("host"), "stage.com"),
                () -> Assertions.assertEquals(history.get(1).get("port"), "5432")
        );
    }

    @Test
    @DisplayName("Test that cache doesn't exist for given tag")
    void testNonExistingTag() {
        var tag = "psql";
        var history = cache.historyPerGroup(
                new TagGroup(
                        new Arguments(
                                new String[]{
                                        tag,
                                        "-host {{reddis:host}}",
                                        "-port {{reddis:port}}",
                                }
                        ),
                        tag
                ));
        Assertions.assertTrue(history.isEmpty());
    }

    @Test
    @DisplayName("Test that cache has all values after persisting to file")
    void testPersistence(@TempDir final Path tempDir) throws IOException {
        var cacheFile = tempDir.resolve("cache.json").toFile();
        var values = Map.of("user", "test", "port", "5432");
        this.saveValues(cacheFile, values);
        var cache = new Cache(cacheFile.getPath());
        Assertions.assertEquals(
                cache.historyPerGroup(
                        new TagGroup("test", List.of("user", "port"))
                ),
                List.of(values)
        );


    }

    /**
     * Save given values in cache file.
     *
     * @param cacheFile Cache file
     * @param values    ResolveFromTerminal
     * @throws IOException If failed
     */
    private void saveValues(final File cacheFile, final Map<String, String> values) throws IOException {
        final Cache cache = new Cache(cacheFile.getPath());
        cache.addEntry("test", values);
        cache.save();
    }
}
