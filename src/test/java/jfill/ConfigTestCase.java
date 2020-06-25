package jfill;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

final class ConfigTestCase {

    private static Config config;

    @BeforeAll
    @DisplayName("Init config")
    static void initConfig() throws IOException {
        config = new Config(testConfig());
    }

    @Test
    @DisplayName("Test that config has two different values for given word")
    void testHistoryForWord() {
        Assertions.assertEquals(
                config.history("user", "psql"),
                List.of("postgres", "admin")
        );

    }

    @Test
    @DisplayName("Test that config doesn't have history for given word")
    void testHistoryIsEmpty() {
        Assertions.assertEquals(
                config.history("connections", "psql"),
                Collections.emptyList()
        );
    }

    @Test
    @DisplayName("Test that both history entries were fetched for given tag")
    void testGroupHistory() {
        var tag = "psql";
        var history = config.historyPerGroup(
                new TagGroup(
                        new Arguments(
                                new String[]{
                                        tag,
                                        "-host {{psql:host}}",
                                        "-port {{psql:port}}",
                                },
                                Defaults.FILLIN_PTN
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
    @DisplayName("Test that config doesn't have history for given tag")
    void testNonExistingTag() {
        var tag = "psql";
        var history = config.historyPerGroup(
                new TagGroup(
                        new Arguments(
                                new String[]{
                                        tag,
                                        "-host {{reddis:host}}",
                                        "-port {{reddis:port}}",
                                },
                                Defaults.FILLIN_PTN
                        ),
                        tag
                ));
        Assertions.assertTrue(history.isEmpty());
    }

    @Test
    void test() {
        config.addEntry(
                "reddis",
                Map.of(
                        "host", "privet",
                        "port", "5432",
                        "user", "postgres"
                )
        );
    }

    private static String testConfig() {
        return ConfigTestCase.class.getClassLoader().getResource("test_fillin.json").getFile();
    }
}