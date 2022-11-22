package io.apicurio.registry.fleetshard.operator.dependent;

import io.apicurio.registry.fleetshard.operator.service.DependentResourceCache;
import io.apicurio.registry.fleetshard.operator.crd.v1.ApicurioRegistryFleetShard;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependentResource;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class RegistryDeploymentDR extends KubernetesDependentResource<Deployment, ApicurioRegistryFleetShard> {

    @Inject
    DependentResourceCache resourceCache;

    public RegistryDeploymentDR() {
        super(Deployment.class);
    }

    @Override
    protected Deployment desired(ApicurioRegistryFleetShard primary, Context<ApicurioRegistryFleetShard> context) {
        return (Deployment) resourceCache.get(DependentResourceCache.ResourceId.REGISTRY_DEPLOYMENT);
    }
}
