package jfill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

final class TagGroup {
    private final List<String> keys;

    private final String tag;

    TagGroup(final String tag, final String key) {
        this.tag = tag;
        this.keys = Collections.singletonList(key);
    }

    TagGroup(final Arguments arguments, final String tag) {
        var cache = new HashMap<String, Boolean>();
        var keys = new ArrayList<String>(16);
        for (var arg : arguments) {
            if (!cache.containsKey(arg.getKey()) && arg.hasTag() && arg.getTag().equals(tag)) {
                cache.put(arg.getKey(), true);
                keys.add(arg.getKey());
            }
        }
        this.keys = keys.isEmpty() ? Collections.emptyList() : keys;
        this.tag = tag;
    }

    List<String> getKeys() {
        return this.keys;
    }

    String getTag() {
        return this.tag;
    }
}
