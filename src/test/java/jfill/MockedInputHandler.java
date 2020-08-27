package jfill;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

final class MockedInputHandler implements InputHandler {

    private final Map<String, String> storage;


    MockedInputHandler(final Map<String, String> storage) {
        this.storage = storage;
    }

    @Override
    public String getValue(final List<String> words, final Suggestions suggestions) {
        return words.stream()
                .map(this.storage::get)
                .collect(Collectors.joining(","));
    }

    @Override
    public String getValue(final String word, final Suggestions suggestions) {
        return this.storage.get(word);
    }
}
