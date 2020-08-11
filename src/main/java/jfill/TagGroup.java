package jfill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Group keys by tag.
 */
final class TagGroup {
    /**
     * Keys.
     */
    private final List<String> keys;

    /**
     * Tag.
     */
    private final String tag;

    TagGroup(final String tag, final String key) {
        this(
                tag,
                Collections.singletonList(key)
        );
    }

    TagGroup(final String tag, final List<String> keys) {
        this.tag = tag;
        this.keys = keys;
    }

    TagGroup(final Arguments arguments, final String tag) {
        var cache = new HashSet<String>();
        for (var arg : arguments) {
            if (!cache.contains(arg.getKey()) && arg.hasTag() && arg.getTag().equals(tag)) {
                cache.add(arg.getKey());
            }
        }
        this.keys = cache.isEmpty() ? Collections.emptyList() : new ArrayList<>(cache);
        this.tag = tag;
    }

    List<String> getKeys() {
        return this.keys;
    }

    String getTag() {
        return this.tag;
    }
}
