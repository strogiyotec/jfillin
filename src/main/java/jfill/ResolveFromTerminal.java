package jfill;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Resolve arguments using input from terminal.
 */
final class ResolveFromTerminal implements ValuesResolver {

    private final Cache cache;

    private final InputHandler terminalInput;

    ResolveFromTerminal(final InputHandler input, final Cache cache) {
        this.cache = cache;
        this.terminalInput = input;
    }

    //TODO simplify
    @Override
    public ResolvedValues resolve(final Arguments arguments) {
        var resolvedValues = new ResolvedValues();
        resolvedValues.addTagIfAbsent(Defaults.NO_TAG);
        for (var arg : arguments) {
            if (arg.hasTag()) {
                resolvedValues.addTagIfAbsent(arg.getTag());
                //if value is already resolved, could happend when command contains duplicates
                if (!resolvedValues.tagHasKey(arg.getTag(), arg.getKey())) {
                    var group = new TagGroup(arguments, arg.getTag());
                    if (!group.getKeys().isEmpty()) {
                        var suggestions = this.cache.historyPerGroup(group);
                        var filteredSuggestions = this.filter(suggestions, group.getKeys());

                        if (!filteredSuggestions.isEmpty()) {
                            var values = this.terminalInput.getValue(
                                group.getKeys(),
                                new Suggestions(filteredSuggestions, ",")
                            ).split(",");
                            //if user didn't choose suggestion or didn't write all values
                            if (values.length != group.getKeys().size()) {
                                this.chooseValuesByOne(resolvedValues, group, suggestions);
                                continue;
                            } else {
                                //if user chose suggestion
                                for (int i = 0; i < group.getKeys().size(); i++) {
                                    resolvedValues.addResolvedValue(group.getTag(), group.getKeys().get(i), values[i]);
                                }
                                continue;
                            }
                        } else {
                            this.chooseValuesByOne(resolvedValues, group, suggestions);
                            continue;
                        }
                    }
                } else {
                    //value is already resolved
                    continue;
                }
            }
            //no tag, use default then
            if (!resolvedValues.tagHasKey(Defaults.NO_TAG, arg.getKey())) {
                var value = this.terminalInput
                    .getValue(
                        arg.getKey(),
                        new Suggestions(
                            this.cache.historyPerKey(
                                Defaults.NO_TAG, arg.getKey()
                            )
                        )
                    );
                resolvedValues.addResolvedValue(Defaults.NO_TAG, arg.getKey(), value);
            }
        }
        return resolvedValues;
    }

    private void chooseValuesByOne(
        final ResolvedValues storage,
        final TagGroup group,
        final List<Map<String, String>> history
    ) {
        group.getKeys().forEach(key -> {
            var value = this.terminalInput.getValue(
                key, new Suggestions(history, map -> map.get(key))
            );
            storage.addResolvedValue(group.getTag(), key, value);
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
     * @param history Cached history
     * @param keys Keys from cli
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
