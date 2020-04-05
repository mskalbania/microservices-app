package edu.application.organization.utils;

import java.util.Objects;

public class TransactionContextHolder {

    private static final ThreadLocal<TransactionContext> transactionCtx = new ThreadLocal<>(); //Each thread get a copy

    public static TransactionContext getCtx() {
        TransactionContext transactionContext = transactionCtx.get();
        if (transactionContext == null) {
            transactionContext = new TransactionContext();
            transactionCtx.set(transactionContext);
        }
        return transactionContext;
    }

    public static void setCtx(TransactionContext transactionContext) {
        Objects.requireNonNull(transactionContext, "TxCtx can't be null");
        transactionCtx.set(transactionContext);
    }
}
