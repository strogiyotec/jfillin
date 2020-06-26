package jfill;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

final class ResolvedValues {

    private final Config config;

    private final InputHandler inputHandler;

    ResolvedValues(final InputHandler input, final Config config) {
        this.config = config;
        this.inputHandler = input;
    }

    void resolve(final Arguments arguments, final ValuesByTagStorage storage, final String defaultTag) {
        storage.addTag(defaultTag);
        for (var arg : arguments) {
            if (arg.hasTag()) {
                storage.addTag(arg.getTag());
                //if not in the cache
                if (!storage.tagHasKey(arg.getTag(), arg.getKey())) {
                    var group = new TagGroup(arguments, arg.getTag());
                    if (!group.getKeys().isEmpty()) {
                        var history = this.config.historyPerGroup(group);
                        var filteredSuggestions = this.filter(history, group.getKeys());
                        //if group history has all keys from group
                        if (!filteredSuggestions.isEmpty()) {
                            var values = this.inputHandler.getValue(group.getKeys(), this.prepareSuggestions(filteredSuggestions)).split(",");
                            //if user didn't choose suggestion or didn't write all values
                            if (values.length != group.getKeys().size()) {
                                this.chooseValuesByOne(storage, group, history);
                                continue;
                            } else {
                                //if user chose suggestion
                                for (int i = 0; i < group.getKeys().size(); i++) {
                                    storage.store(group.getTag(), group.getKeys().get(i), values[i]);
                                }
                                continue;
                            }
                        } else {
                            this.chooseValuesByOne(storage, group, history);
                            continue;
                        }
                    }
                } else {
                    //if in cache then go to the next key
                    continue;
                }
            }
            if (!storage.tagHasKey(defaultTag, arg.getKey())) {
                var value = this.inputHandler
                        .getValue(
                                arg.getKey(),
                                this.config.history(
                                        arg.getKey(), defaultTag)
                        );
                storage.store(defaultTag, arg.getKey(), value);
            }
        }
    }

    private void chooseValuesByOne(final ValuesByTagStorage storage, final TagGroup group, final List<Map<String, String>> history) {
        group.getKeys().forEach(key -> {
            var value = this.inputHandler.getValue(
                    key, this.prepareSuggestions(history, key)
            );
            storage.store(group.getTag(), key, value);
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
