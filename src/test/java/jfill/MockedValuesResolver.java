package jfill;

public final class MockedValuesResolver implements ValuesResolver {

    private final ResolvedValuesStorage storage;

    MockedValuesResolver(final ResolvedValuesStorage storage) {
        this.storage = storage;
    }

    @Override
    public ResolvedValuesStorage resolve(final Arguments arguments) {
        return this.storage;
    }
}
