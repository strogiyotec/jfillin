package jfill;

public final class Main {

    /**
     * Utility class.
     */
    private Main() {

    }

    public static void main(final String[] args) throws Exception {
        new Execution(
                args,
                Defaults.NO_TAG,
                Defaults.FILLIN_PTN,
                new Cache(Defaults.CACHE_PATH)
        ).execute();
    }
}
