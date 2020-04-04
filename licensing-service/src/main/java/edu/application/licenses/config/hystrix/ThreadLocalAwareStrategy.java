package edu.application.licenses.config.hystrix;

import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariable;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableLifecycle;
import com.netflix.hystrix.strategy.properties.HystrixProperty;
import edu.application.licenses.utils.TransactionContextHolder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadLocalAwareStrategy extends HystrixConcurrencyStrategy {

    private final HystrixConcurrencyStrategy springStrategy;

    public ThreadLocalAwareStrategy(HystrixConcurrencyStrategy springStrategy) {
        this.springStrategy = springStrategy;
    }

    @Override
    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey, HystrixProperty<Integer> corePoolSize, HystrixProperty<Integer> maximumPoolSize, HystrixProperty<Integer> keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        return springStrategy != null ?
                springStrategy.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue) :
                super.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey, HystrixThreadPoolProperties threadPoolProperties) {
        return springStrategy != null ?
                springStrategy.getThreadPool(threadPoolKey, threadPoolProperties) :
                super.getThreadPool(threadPoolKey, threadPoolProperties);
    }

    @Override
    public BlockingQueue<Runnable> getBlockingQueue(int maxQueueSize) {
        return springStrategy != null ?
                springStrategy.getBlockingQueue(maxQueueSize) :
                super.getBlockingQueue(maxQueueSize);
    }

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        //This is invoked via parent thread so transaction context is present
        return springStrategy != null ?
                springStrategy.wrapCallable(new DelegatingTransactionContextCallable<>(TransactionContextHolder.getCtx(), callable)) :
                super.wrapCallable(new DelegatingTransactionContextCallable<>(TransactionContextHolder.getCtx(), callable));
    }

    @Override
    public <T> HystrixRequestVariable<T> getRequestVariable(HystrixRequestVariableLifecycle<T> rv) {
        return springStrategy != null ?
                springStrategy.getRequestVariable(rv) :
                super.getRequestVariable(rv);
    }
}
