package jfill;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public final class Values {

    private final Config config;

    private InputHandler inputHandler;

    public Values(final InputHandler input, final Config config) {
        this.config = config;
        this.inputHandler = input;
    }

    public Map<String, Map<String, String>> resolve(final Arguments arguments, final String defaultTag) {
        //cache already inserted values.
        //key is tag ,value is map where key is cli key and value is value from input
        var cachedValues = new HashMap<String, Map<String, String>>();
        cachedValues.computeIfAbsent(defaultTag, s -> new HashMap<>());
        for (var arg : arguments) {
            if (arg.hasTag()) {
                cachedValues.computeIfAbsent(arg.getTag(), s -> new HashMap<>());
                //if not in the cache
                if (!cachedValues.get(arg.getTag()).containsKey(arg.getKey())) {
                    var group = new TagGroup(arguments, arg.getTag());
                    if (!group.getKeys().isEmpty()) {
                        var history = this.config.historyPerGroup(group);
                        var filteredSuggestions = this.filter(history, group.getKeys());
                        //if group history has all keys from group
                        if (!filteredSuggestions.isEmpty()) {
                            var values = this.inputHandler.getValue(group.getKeys(), this.prepareSuggestions(history)).split(", ");
                            //if user didn't choose suggestion or didn't write all values
                            if (values.length != history.size()) {
                                this.chooseValuesByOne(cachedValues, group, history);
                                continue;
                            } else {
                                //if user chose suggestion
                                for (int i = 0; i < group.getKeys().size(); i++) {
                                    cachedValues.get(group.getTag()).put(group.getKeys().get(i), values[i]);
                                }
                                continue;
                            }
                        } else if (!history.isEmpty()) {
                            //otherwise fill values by one
                            this.chooseValuesByOne(cachedValues, group, history);
                            continue;
                        }
                    }
                } else {
                    //if in cache then go to the next key
                    continue;
                }
            }
            if (!cachedValues.get(defaultTag).containsKey(arg.getKey())) {
                cachedValues.get(defaultTag)
                        .put(
                                arg.getKey(),
                                this.inputHandler.getValue(
                                        arg.getKey(),
                                        this.config.history(
                                                arg.getKey(), defaultTag)
                                )
                        );
            }
        }
        return cachedValues;
    }

    private void chooseValuesByOne(final Map<String, Map<String, String>> cachedValues, final TagGroup group, final List<Map<String, String>> history) {
        group.getKeys().forEach(key -> {
            var value = this.inputHandler.getValue(key, this.prepareSuggestions(history, key));
            cachedValues.get(group.getTag()).put(key, value);
        });
    }

    private List<Map<String, String>> filter(final List<Map<String, String>> history, final List<String> keys) {
        return history.stream()
                .filter(map -> map.keySet().containsAll(keys))
                .collect(Collectors.toList());
    }

    private List<String> prepareSuggestions(final List<Map<String, String>> history) {
        return history.stream().map(map -> String.join(", ", map.values())).collect(Collectors.toList());
    }

    private List<String> prepareSuggestions(final List<Map<String, String>> history, final String key) {
        return history.stream().map(map -> map.get(key)).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
