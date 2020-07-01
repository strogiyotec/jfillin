package jfill;

public final class Utils {

    static String configPath(final String fileName) {
        return Utils.class.getClassLoader().getResource(fileName).getFile();
    }
}
