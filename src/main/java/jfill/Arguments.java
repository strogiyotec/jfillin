package jfill;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final class Arguments implements Iterable<Argument> {

    private final List<Argument> arguments;

    Arguments(final String[] args) {
        var arguments = new ArrayList<Argument>(16);
        for (var param : args) {
            var matcher = Defaults.FILLIN_PTN.matcher(param);
            if (matcher.find()) {
                var word = matcher.group(2);
                if (word.contains(":")) {
                    var parts = word.split(":");
                    arguments.add(new Argument(parts[1], parts[0]));
                } else {
                    //without tag
                    arguments.add(new Argument(word));
                }
            }
        }
        this.arguments = arguments;
    }

    @Override
    public Iterator<Argument> iterator() {
        return this.arguments.iterator();
    }
}
