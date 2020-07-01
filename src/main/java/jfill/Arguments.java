package jfill;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Arguments implements Iterable<Argument> {

    private final List<Argument> arguments;

    Arguments(final String[] args) {
        final List<Argument> arguments = new ArrayList<>(16);
        for (final String param : args) {
            var matcher = Defaults.FILLIN_PTN.matcher(param);
            if (matcher.find()) {
                final String word = matcher.group(1);
                if (word.contains(":")) {
                    final String[] parts = word.split(":");
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
