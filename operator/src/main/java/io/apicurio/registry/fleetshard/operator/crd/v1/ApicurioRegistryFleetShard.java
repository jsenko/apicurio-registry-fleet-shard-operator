package io.apicurio.registry.fleetshard.operator.crd.v1;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Kind;
import io.fabric8.kubernetes.model.annotation.Version;

@Group("io.apicurio")
@Kind("ApicurioRegistryFleetShard")
@Version("v1")
public class ApicurioRegistryFleetShard extends CustomResource<ApicurioRegistryFleetShardSpec, ApicurioRegistryFleetShardStatus> implements Namespaced {
}
