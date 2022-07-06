package nl.oosterhuis.bookapp.exception;

public class TransactionNotFoundException extends Exception {
    public TransactionNotFoundException() {
    }

    public TransactionNotFoundException(Long transactionId) {
        super("Transaction with id '" + transactionId + "' does not exist.");
    }
}