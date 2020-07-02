package jfill;

import java.util.regex.Pattern;

public interface Defaults {

    Pattern FILLIN_PTN = Pattern.compile("\\{\\{(.*)}}");

    String NO_TAG = "noTag";

    String VERSION = "jfillin 1.0";

    String CACHE_PATH = System.getProperty("user.home") + "/.config/jfillin/fillin.json";
}
