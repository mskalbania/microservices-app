package edu.application.licenses.config.hystrix;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class HystrixConfiguration {

    private HystrixConcurrencyStrategy springStrategy;

    public HystrixConfiguration(@Autowired(required = false) HystrixConcurrencyStrategy springStrategy) {
        this.springStrategy = springStrategy;
    }

    @PostConstruct
    public void init() { //Reregistration of hystrix plugins with new concurrency strategy
        HystrixPlugins plugins = HystrixPlugins.getInstance();
        HystrixCommandExecutionHook commandExecutionHook = plugins.getCommandExecutionHook();
        HystrixEventNotifier eventNotifier = plugins.getEventNotifier();
        HystrixMetricsPublisher metricsPublisher = plugins.getMetricsPublisher();
        HystrixPropertiesStrategy propertiesStrategy = plugins.getPropertiesStrategy();
        HystrixPlugins.reset();
        plugins.registerCommandExecutionHook(commandExecutionHook);
        plugins.registerConcurrencyStrategy(new ThreadLocalAwareStrategy(springStrategy));
        plugins.registerEventNotifier(eventNotifier);
        plugins.registerMetricsPublisher(metricsPublisher);
        plugins.registerPropertiesStrategy(propertiesStrategy);
    }
}
