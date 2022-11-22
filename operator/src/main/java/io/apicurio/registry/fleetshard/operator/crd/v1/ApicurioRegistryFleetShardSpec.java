package io.apicurio.registry.fleetshard.operator.crd.v1;

import io.fabric8.kubernetes.api.model.Secret;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@ToString
@EqualsAndHashCode
public class ApicurioRegistryFleetShardSpec {

    private String controlPlaneApiUrl;

    private List<ApicurioRegistryFleetShardSpecConfigItem> config;

    private Secret subscription;

    public String getControlPlaneApiUrl() {
        return controlPlaneApiUrl;
    }

    public void setControlPlaneApiUrl(String controlPlaneApiUrl) {
        this.controlPlaneApiUrl = controlPlaneApiUrl;
    }

    public List<ApicurioRegistryFleetShardSpecConfigItem> getConfig() {
        return config;
    }

    public void setConfig(List<ApicurioRegistryFleetShardSpecConfigItem> config) {
        this.config = config;
    }

    public Secret getSubscription() {
        return subscription;
    }

    public void setSubscription(Secret subscription) {
        this.subscription = subscription;
    }
}
