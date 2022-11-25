package io.apicurio.registry.fleetshard.operator.service;

import io.apicurio.registry.fleetshard.operator.common.SerDesObjectMapperProducer;
import io.fabric8.kubernetes.api.model.apps.Deployment;

import java.io.IOException;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ResourceFactory {

    private <T> T loadResource(String path, Class<T> type) {
        path = "/default/" + path;
        try {
            return SerDesObjectMapperProducer.getYAMLMapper().readValue(
                    getClass().getResourceAsStream(path), type);
        } catch (IOException ex) {
            throw new RuntimeException("Could not load " + path, ex);
        }
    }

    public Deployment createRegistryDeployment() {
        return loadResource("registry/registry-deployment.yaml", Deployment.class);
    }

    public Deployment createTestDeployment() {
        return loadResource("test/busybox-deployment.yaml", Deployment.class);
    }
}
