package jfill;

import mjson.Json;
import org.jline.reader.impl.LineReaderImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class ShellCommand {

    private final Cache cache;

    private InputHandler inputHandler;

    public ShellCommand(final InputHandler input, final Cache cache) {
        this.cache = cache;
        this.inputHandler = input;
    }

    /**
     * Create final shell command.
     * Replace all words by given pattern with
     * values given by user and then join
     * all words to single shell command
     *
     * @param args    Args
     * @param pattern Pattern
     * @return Shell command
     */
    public String join(final String[] args, Pattern pattern) throws IOException {
        var words = new ArrayList<String>(16);
        var cache = new HashMap<String, String>();
        var config = this.cache.read();
        for (var arg : args) {
            var matcher = pattern.matcher(arg);
            if (matcher.matches()) {
                var word = matcher.group(1);
                if (!cache.containsKey(word)) {
                    var value = this.getValue(config, word);
                    words.add(value);
                    cache.put(word, value);
                } else {
                    words.add(word);
                }
            } else {
                words.add(arg);
            }
        }
        return String.join(" ", words);
    }

    private String getValue(final Json config, final String word) {
        //has tag
        if (word.contains(":")) {
            return null;
        } else {
            final Json cachedValues = config.at(Config.NO_TAG).at(word,Json.nil());
            if (!cachedValues.isNull()) {
                return this.inputHandler.getValue(word, cachedValues(cachedValues));
            } else {
                return this.inputHandler.getValue(word, Collections.emptyList());
            }
        }
    }

    private static List<String> cachedValues(final Json json) {
        return json.asList()
                .stream()
                .map(Object::toString)
                .collect(Collectors.toUnmodifiableList());
    }

    private String fillValue(final String word, final LineReaderImpl reader) {
        return reader.readLine(String.format("%s: ", word));
    }

}
