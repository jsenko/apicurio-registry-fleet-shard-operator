package io.apicurio.registry.fleetshard.operator.control.impl;

import io.apicurio.registry.fleetshard.operator.control.ControlFunction;
import io.apicurio.registry.fleetshard.operator.service.DependentResourceCache;
import io.apicurio.registry.fleetshard.operator.service.ResourceFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CreateRegistryDeploymentCF implements ControlFunction {

    @Inject
    DependentResourceCache resourceCache;

    @Inject
    ResourceFactory resourceFactory;

    @Override
    public void sense() {
    }

    @Override
    public boolean updateNeeded() {
        return !resourceCache.contains(DependentResourceCache.ResourceId.REGISTRY_DEPLOYMENT);
    }

    @Override
    public void update() {
        var deployment = resourceFactory.createRegistryDeployment();
        resourceCache.put(DependentResourceCache.ResourceId.REGISTRY_DEPLOYMENT, deployment);
    }

    @Override
    public boolean cleanupNeeded() {
        return false;
    }
}
