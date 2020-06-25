package jfill;

import java.util.Optional;

/**
 * Cli argument.
 */
public final class Argument {

    /**
     * Key.
     */
    private final String key;

    /**
     * Tag.
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<String> tag;

    Argument(final String key, final String tag) {
        this.key = key;
        this.tag = Optional.of(tag);
    }

    Argument(final String key) {
        this.key = key;
        this.tag = Optional.empty();
    }

    String getKey() {
        return key;
    }

    boolean hasTag() {
        return this.tag.isPresent();
    }

    String getTag() {
        return this.tag.orElseThrow();
    }
}
