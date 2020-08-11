package jfill;

final class Utils {

    static String testConfigPath(final String fileName) {
        return Utils.class.getClassLoader().getResource(fileName).getFile();
    }
}
