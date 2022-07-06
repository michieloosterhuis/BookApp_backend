package nl.oosterhuis.bookapp.model;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum Language {
    DUTCH,
    ENGLISH,
    GERMAN,
    FRENCH;

    private static final Set<String> strLanguages =
            Arrays.stream(values())
                    .map(Language::name)
                    .collect(Collectors.toSet());

    public static boolean isValid(String val) {
        return strLanguages.contains(val);
    }
}
