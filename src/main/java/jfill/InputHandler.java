package jfill;

import java.util.List;

interface InputHandler {

    String getValue(List<String> words, Suggestions suggestions);

    String getValue(String word, Suggestions suggestions);
}
