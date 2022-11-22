package io.apicurio.registry.fleetshard.operator.service;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.javaoperatorsdk.operator.api.reconciler.dependent.DependentResource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DependentResourceCache {

    private Set<DependentResource> resources = new HashSet<>();

    private Map<ResourceId, HasMetadata> resources2 = new HashMap<>();

    public boolean contains(ResourceId id) {
        return resources2.containsKey(id);
    }

    public void put(ResourceId key, HasMetadata value) {
        resources2.put(key, value);
    }

    public HasMetadata get(ResourceId key) {
        return resources2.get(key);
    }

    public enum ResourceId {
        REGISTRY_DEPLOYMENT,
        REGISTRY_SERVICE,
    }
}
