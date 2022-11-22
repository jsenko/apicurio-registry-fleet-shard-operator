package io.apicurio.registry.fleetshard.operator.reconciler;

import io.apicurio.registry.fleetshard.operator.crd.v1.ApicurioRegistryFleetShard;
import io.apicurio.registry.fleetshard.operator.crd.v1.ApicurioRegistryFleetShardStatus;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.ErrorStatusHandler;
import io.javaoperatorsdk.operator.api.reconciler.ErrorStatusUpdateControl;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ControllerConfiguration
public class ApicurioRegistryFleetShardReconciler implements Reconciler<ApicurioRegistryFleetShard>, ErrorStatusHandler<ApicurioRegistryFleetShard> {

    final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public UpdateControl<ApicurioRegistryFleetShard> reconcile(ApicurioRegistryFleetShard schema, Context<ApicurioRegistryFleetShard> context) {
        return UpdateControl.noUpdate();
    }

    @Override
    public ErrorStatusUpdateControl<ApicurioRegistryFleetShard> updateErrorStatus(ApicurioRegistryFleetShard schema,
                                                                                  Context<ApicurioRegistryFleetShard> context,
                                                                                  Exception e) {
        ApicurioRegistryFleetShardStatus status = new ApicurioRegistryFleetShardStatus();
        status.setStatus("ERROR: " + e.getMessage());
        schema.setStatus(status);
        return ErrorStatusUpdateControl.updateStatus(schema);
    }
}
