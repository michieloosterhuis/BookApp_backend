package nl.oosterhuis.bookapp.model;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum TransactionType {
    GIFT,
    EXCHANGE_FOR_BOOK,
    EXCHANGE_FOR_CAKE;

    private static final Set<String> strTransactionTypes =
            Arrays.stream(values())
                    .map(TransactionType::name)
                    .collect(Collectors.toSet());

    public static boolean isValid(String val) {
        return strTransactionTypes.contains(val);
    }
}
