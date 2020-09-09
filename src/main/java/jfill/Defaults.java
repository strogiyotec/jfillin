package jfill;

import java.util.Optional;
import java.util.regex.Pattern;

interface Defaults {

    Pattern FILLIN_PTN = Pattern.compile("(.*)?\\{\\{(.*)}}([^ ]+)?");

    String NO_TAG = "noTag";

    String VERSION = "jfillin 2.2.2";

    String CACHE_PATH = Optional.ofNullable(System.getenv("XDG_CONFIG_HOME"))
            .or(() -> Optional.of(System.getProperty("user.home")))
            .map(path -> path + "/jfillin/fillin.json")
            .get();

}
