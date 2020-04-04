package edu.application.licenses.config.hystrix;

import edu.application.licenses.utils.TransactionContext;
import edu.application.licenses.utils.TransactionContextHolder;

import java.util.concurrent.Callable;

public class DelegatingTransactionContextCallable<V> implements Callable<V> {

    private TransactionContext parentThreadTxCtx;
    private Callable<V> delegate;

    public DelegatingTransactionContextCallable(TransactionContext parentThreadTxCtx, Callable<V> delegate) {
        this.parentThreadTxCtx = parentThreadTxCtx;
        this.delegate = delegate;
    }

    @Override
    public V call() throws Exception {
        TransactionContextHolder.setCtx(parentThreadTxCtx); //This is invoked by hystrix pool thread so now parent thread ctx will be set
        try {
            return delegate.call();
        } finally {
            parentThreadTxCtx = null; //This will reset thread transaction ctx so new can be set when new tx comes in
        }
    }
}
