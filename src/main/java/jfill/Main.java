package jfill;

import java.util.Scanner;
import java.util.regex.Pattern;

public final class Main {
    public static final Pattern FILLIN_PTN = Pattern.compile("\\{\\{(.*)}}");

    public static void main(String[] args) {
        System.out.println(
                new ShellCommand().join(
                        args, FILLIN_PTN, new Scanner(System.in), System.out
                )
        );
    }
}
