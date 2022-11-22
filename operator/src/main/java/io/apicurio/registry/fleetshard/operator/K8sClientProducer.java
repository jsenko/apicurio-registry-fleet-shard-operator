package io.apicurio.registry.fleetshard.operator;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class K8sClientProducer {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Produces
    @ApplicationScoped
    public KubernetesClient produce() {
        return new DefaultKubernetesClient();
    }
}
