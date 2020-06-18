package jfill;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public final class ShellCommand {

    /**
     * Create final shell command.
     * Replace all words by given pattern with
     * values given by user and then join
     * all words to single shell command
     *
     * @param args    Args
     * @param pattern Pattern
     * @param scanner Scanner
     * @return Shell command
     */
    public String join(final String[] args, final Pattern pattern, final Scanner scanner, final PrintStream out) {
        var words = new ArrayList<String>(16);
        var cache = new HashMap<String, String>();
        for (final String arg : args) {
            var matcher = pattern.matcher(arg);
            if (matcher.matches()) {
                var word = matcher.group(1);
                if (!cache.containsKey(word)) {
                    var value = this.fillValue(word, scanner, out);
                    words.add(value);
                    cache.put(word, value);
                } else {
                    words.add(word);
                }
            } else {
                words.add(arg);
            }
        }
        return String.join(" ", words);
    }

    private String fillValue(final String word, final Scanner scanner, final PrintStream out) {
        out.printf("%s: ", word);
        final String value = scanner.next();
        out.println();
        return value;
    }

}
