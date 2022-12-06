package io.apicurio.registry.fleetshard.operator.crd.v1;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Kind;
import io.fabric8.kubernetes.model.annotation.Version;

import static java.util.Objects.requireNonNull;

@Group("registry.apicur.io")
@Kind("FleetShard")
@Version("v1")
public class ApicurioRegistryFleetShard extends CustomResource<ApicurioRegistryFleetShardSpec, ApicurioRegistryFleetShardStatus> implements Namespaced {

    public String getCRScopeKey() {
        requireNonNull(getMetadata().getNamespace());
        requireNonNull(getMetadata().getName());
        return getMetadata().getNamespace() + "/" + getMetadata().getName();
    }
}
