package jfill;

import java.util.Optional;

public final class Argument {

    private final String key;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<String> tag;

    public Argument(final String key, final String tag) {
        this.key = key;
        this.tag = Optional.of(tag);
    }

    public Argument(final String key) {
        this.key = key;
        this.tag = Optional.empty();
    }

    public String getKey() {
        return key;
    }

    public boolean hasTag() {
        return this.tag.isPresent();
    }

    public String getTag() {
        return this.tag.orElseThrow();
    }
}
