package org.bazaar.giza.transaction.exception;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(Long transactionId) { super("Transaction with ID " + transactionId + " not found.");
    }
}