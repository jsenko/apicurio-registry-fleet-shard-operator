package io.apicurio.registry.fleetshard.operator;

import io.javaoperatorsdk.operator.Operator;
import io.quarkus.runtime.StartupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

public class Startup {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    Operator operator;

    public void onStart(@Observes StartupEvent ev) {
        log.info("Starting Apicurio Registry Fleet Shard Operator");
        operator.start();
        operator.installShutdownHook();
    }
}
