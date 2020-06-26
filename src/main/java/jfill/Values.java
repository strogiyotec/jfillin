package jfill;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

final class Values {

    private final Config config;

    private final InputHandler inputHandler;

    Values(final InputHandler input, final Config config) {
        this.config = config;
        this.inputHandler = input;
    }

    Map<String, Map<String, String>> resolve(final Arguments arguments, final String defaultTag) {
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
                            var values = this.inputHandler.getValue(group.getKeys(), this.prepareSuggestions(filteredSuggestions)).split(",");
                            //if user didn't choose suggestion or didn't write all values
                            if (values.length != group.getKeys().size()) {
                                this.chooseValuesByOne(cachedValues, group, history);
                                continue;
                            } else {
                                //if user chose suggestion
                                for (int i = 0; i < group.getKeys().size(); i++) {
                                    cachedValues.get(group.getTag()).put(group.getKeys().get(i), values[i].trim());
                                }
                                continue;
                            }
                        } else {
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
                var value = this.inputHandler
                        .getValue(
                                arg.getKey(),
                                this.config.history(
                                        arg.getKey(), defaultTag)
                        );
                cachedValues.get(defaultTag)
                        .put(
                                arg.getKey(),
                                value.trim()
                        );
            }
        }
        return cachedValues;
    }

    private void chooseValuesByOne(final Map<String, Map<String, String>> cachedValues, final TagGroup group, final List<Map<String, String>> history) {
        group.getKeys().forEach(key -> {
            var value = this.inputHandler.getValue(
                    key, this.prepareSuggestions(history, key)
            );
            cachedValues.get(group.getTag()).put(key, value.trim());
        });
    }

    private List<Map<String, String>> filter(final List<Map<String, String>> history, final List<String> keys) {
        return history.stream()
                .filter(map -> map.keySet().containsAll(keys))
                .collect(Collectors.toList());
    }

    private List<String> prepareSuggestions(final List<Map<String, String>> history) {
        return history.stream().map(map -> String.join(",", map.values())).collect(Collectors.toList());
    }

    private List<String> prepareSuggestions(final List<Map<String, String>> history, final String key) {
        return history.stream().map(map -> map.get(key)).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
