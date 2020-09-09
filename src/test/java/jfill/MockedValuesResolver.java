package jfill;

final class MockedValuesResolver implements ValuesResolver {

    private final ResolvedValues storage;

    MockedValuesResolver(final ResolvedValues storage) {
        this.storage = storage;
    }

    @Override
    public ResolvedValues resolve(final Arguments arguments) {
        return this.storage;
    }
}
