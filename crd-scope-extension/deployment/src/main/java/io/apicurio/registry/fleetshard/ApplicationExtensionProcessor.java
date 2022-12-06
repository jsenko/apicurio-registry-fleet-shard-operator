package io.apicurio.registry.fleetshard;

import io.quarkus.arc.deployment.ContextRegistrationPhaseBuildItem;
import io.quarkus.arc.deployment.ContextRegistrationPhaseBuildItem.ContextConfiguratorBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

public class ApplicationExtensionProcessor {
    static final String FEATURE_NAME = "apicurio-registry-fleet-shard-operator-cdr-scope-extension";

    @BuildStep
    FeatureBuildItem createFeatureItem() {
        return new FeatureBuildItem(FEATURE_NAME);
    }

    @BuildStep
    public ContextConfiguratorBuildItem customContext(ContextRegistrationPhaseBuildItem item) {
        return new ContextConfiguratorBuildItem(
                item.getContext()
                        .configure(CRScoped.class)
                        .normal()
                        .contextClass(CRScopeContext.class)
        );
    }
}
