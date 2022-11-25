package io.apicurio.registry.fleetshard.operator.control.impl;

import io.apicurio.registry.fleetshard.CRScoped;
import io.apicurio.registry.fleetshard.operator.control.ControlFunction;
import io.apicurio.registry.fleetshard.operator.service.ResourceCache;
import io.apicurio.registry.fleetshard.operator.service.ResourceFactory;
import io.fabric8.kubernetes.client.KubernetesClient;

import javax.inject.Inject;

@CRScoped
public class CreateRegistryDeploymentCF implements ControlFunction {

    @Inject
    ResourceCache resourceCache;

    @Inject
    ResourceFactory resourceFactory;

    @Inject
    KubernetesClient client;

    @Override
    public void sense() {
    }

    @Override
    public boolean updateNeeded() {
        return !resourceCache.contains(ResourceCache.ResourceId.REGISTRY_DEPLOYMENT);
    }

    @Override
    public void update() {
        var deployment = resourceFactory.createRegistryDeployment();
        //resourceCache.put(ResourceCache.ResourceId.REGISTRY_DEPLOYMENT, deployment);
        client.apps().deployments()/*.inNamespace("TODO")*/.create(deployment);
    }

    @Override
    public boolean cleanupNeeded() {
        return false;
    }
}
