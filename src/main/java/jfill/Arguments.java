package jfill;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Jfill args.
 */
final class Arguments implements Iterable<Argument> {

    private final List<Argument> arguments;

    Arguments(final String[] args) {
        var jfillArgs = new ArrayList<Argument>(16);
        for (var param : args) {
            var matcher = Defaults.FILLIN_PTN.matcher(param);
            if (matcher.find()) {
                var word = matcher.group(2);
                if (word.contains(":")) {
                    //with tag
                    var parts = word.split(":");
                    assert parts.length == 2;
                    jfillArgs.add(new Argument(parts[1], parts[0]));
                } else {
                    //without tag
                    jfillArgs.add(new Argument(word));
                }
            }
        }
        this.arguments = jfillArgs;
    }

    @Override
    public Iterator<Argument> iterator() {
        return this.arguments.iterator();
    }
}
