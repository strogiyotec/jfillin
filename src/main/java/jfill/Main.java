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
                new Cache(Defaults.CACHE_PATH),
                new ProcessBuilder().inheritIO(),
                System.out
        ).execute();
    }
}
