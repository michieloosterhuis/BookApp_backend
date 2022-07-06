package nl.oosterhuis.bookapp.model;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum TransactionStatusCode {
    INITIALIZED,
    EXCHANGE_BOOK_SELECTED,
    FINALIZED;

    private static final Set<String> strTransactionStatusCodes =
            Arrays.stream(values())
                    .map(TransactionStatusCode::name)
                    .collect(Collectors.toSet());

    public static boolean isValid(String val) {
        return strTransactionStatusCodes.contains(val);
    }
}
