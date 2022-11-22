package io.apicurio.registry.fleetshard.operator.crd.v1;

import io.javaoperatorsdk.operator.api.ObservedGenerationAwareStatus;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class ApicurioRegistryFleetShardStatus extends ObservedGenerationAwareStatus {

    private String status; // TODO Conditions

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
