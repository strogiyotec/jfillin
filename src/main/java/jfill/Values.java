package jfill;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Fill command with user specified values.
 */
final class Values {

    private final Cache cache;

    private final InputHandler inputHandler;

    Values(final InputHandler input, final Cache cache) {
        this.cache = cache;
        this.inputHandler = input;
    }

    ValuesStorage resolve(final Arguments arguments, final String defaultTag) {
        final ValuesStorage storage = new ValuesStorage();
        storage.addTag(defaultTag);
        for (var arg : arguments) {
            if (arg.hasTag()) {
                storage.addTag(arg.getTag());
                //if not in the storage
                if (!storage.tagHasKey(arg.getTag(), arg.getKey())) {
                    var group = new TagGroup(arguments, arg.getTag());
                    if (!group.getKeys().isEmpty()) {
                        var history = this.cache.historyPerGroup(group);
                        var filteredSuggestions = this.filter(history, group.getKeys());
                        if (!filteredSuggestions.isEmpty()) {
                            var values = this.inputHandler.getValue(group.getKeys(), new Suggestions.JoinedHistory(filteredSuggestions)).split(",");
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
                    //value is already in storage
                    continue;
                }
            }
            if (!storage.tagHasKey(defaultTag, arg.getKey())) {
                var value = this.inputHandler
                        .getValue(
                                arg.getKey(),
                                new Suggestions.Plain(
                                        this.cache.history(
                                               new TagGroup(defaultTag,arg.getKey())
                                        )
                                )
                        );
                storage.store(defaultTag, arg.getKey(), value);
            }
        }
        return storage;
    }

    private void chooseValuesByOne(
            final ValuesStorage storage,
            final TagGroup group,
            final List<Map<String, String>> history
    ) {
        group.getKeys().forEach(key -> {
            var value = this.inputHandler.getValue(
                    key, new Suggestions.ByKey(history, key)
            );
            storage.store(group.getTag(), key, value);
        });
    }

    /**
     * Filter history.
     * Choose only those entries that have all keys from specified list
     * For example if history has two entries:
     * {
     * "user":"postgres",
     * "port":"5432"
     * }
     * but keys have only one entry(port) then history will be skipped
     *
     * @param history Cached history
     * @param keys    Keys from cli
     * @return Filtered history
     */
    private List<Map<String, String>> filter(
            final List<Map<String, String>> history,
            final List<String> keys
    ) {
        return history.stream()
                .filter(map -> map.keySet().containsAll(keys))
                .collect(Collectors.toList());
    }
}
