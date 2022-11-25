package io.apicurio.registry.fleetshard.operator.control;

import io.apicurio.registry.fleetshard.CRScoped;
import io.apicurio.registry.fleetshard.operator.crd.v1.ApicurioRegistryFleetShard;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import lombok.Getter;
import lombok.Setter;

@CRScoped
public class ContextHolder {

    @Setter
    @Getter
    private Context<ApicurioRegistryFleetShard> context;
}
