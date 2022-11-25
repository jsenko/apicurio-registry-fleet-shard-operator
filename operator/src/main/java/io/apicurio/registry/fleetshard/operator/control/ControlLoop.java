package io.apicurio.registry.fleetshard.operator.control;

import io.apicurio.registry.fleetshard.CRScoped;
import io.apicurio.registry.fleetshard.operator.control.ControlFunction;
import io.apicurio.registry.fleetshard.operator.crd.v1.ApicurioRegistryFleetShard;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@CRScoped
public class ControlLoop {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    Instance<ControlFunction> functions;

    @PostConstruct
    void init() {
        // TODO Sort
    }

    public void run() {
        var cfs = functions.stream().collect(Collectors.toList());
        var MAX_ATTEMPTS = cfs.size() * 2;
        var i = 0;
        for (; i < 100; i++) {

            var stable = true;
            for (ControlFunction function : cfs) {
                if (function.updateNeeded()) {
                    function.update();
                    stable = false;
                }
            }
            if (stable) {
                log.debug("Control loop is stable");
                break;
            }
        }
        if (i == MAX_ATTEMPTS) {
            log.error("Control loop stabilization limit exceeded");
            throw new IllegalStateException("TODO");
        }
    }

    public void runCleanup() {

    }
}
