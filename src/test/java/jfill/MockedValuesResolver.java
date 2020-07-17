package jfill;

public final class MockedValuesResolver implements ValuesResolver {

    private final ValuesStorage storage;

    MockedValuesResolver(final ValuesStorage storage) {
        this.storage = storage;
    }

    @Override
    public ValuesStorage resolve(final Arguments arguments) {
        return this.storage;
    }
}
