package io.apicurio.registry.fleetshard.operator;

import io.apicurio.registry.fleetshard.operator.reconciler.ApicurioRegistryFleetShardReconciler;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.Operator;
import io.javaoperatorsdk.operator.monitoring.micrometer.MicrometerMetrics;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class OperatorProducer {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    KubernetesClient k8sClient;

    @Inject
    ApicurioRegistryFleetShardReconciler reconciler;

    @Produces
    @ApplicationScoped
    public Operator produceOperator() {

        Operator operator = new Operator(k8sClient,
                overrider -> overrider.withMetrics(new MicrometerMetrics(new LoggingMeterRegistry())));

        operator.register(reconciler);

        return operator;
    }
}
