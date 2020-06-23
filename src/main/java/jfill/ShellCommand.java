package jfill;

import mjson.Json;
import org.jline.reader.impl.LineReaderImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public String join(final Arguments arguments, Pattern pattern, final Cache cache) throws IOException {
        var words = new ArrayList<String>(16);
        //cache already inserted values.
        var savedValues = new HashMap<String, String>();
        for (var arg : arguments) {
            if (arg.hasTag()) {
                final TagGroup group = new TagGroup(arguments, arg.getTag());
                if (!group.getKeys().isEmpty()) {
                    final List<String> history = cache.pairHistory(group);
                    if (!history.isEmpty()) {
                        final String value = this.inputHandler.getValue(String.join(", ", group.getKeys()), history);
                        final String[] split = value.split(", ");
                        if (split.length != history.size()) {
                            this.collectSeparately(savedValues, arg.getKey(), group, history);
                        } else {

                        }
                    }

                }
            }
        }
        return String.join(" ", words);
    }

    private void collectSeparately(final Map<String, String> savedValues, final String key, final TagGroup group, final List<String> history) {
        for (int i = 0; i < group.getKeys().size(); i++) {
            final String value1 = this.inputHandler.getValue(key, suggestions(history, i));
            savedValues.put(key, value1);
        }
    }

    private static List<String> suggestions(final List<String> history, final int index) {
        return history.stream().map(line -> line.split(", ")[index]).collect(Collectors.toList());
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
