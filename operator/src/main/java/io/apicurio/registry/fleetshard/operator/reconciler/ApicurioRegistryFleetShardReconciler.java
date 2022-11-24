package io.apicurio.registry.fleetshard.operator.reconciler;

import io.apicurio.registry.fleetshard.operator.control.impl.ControlLoop;
import io.apicurio.registry.fleetshard.operator.crd.v1.ApicurioRegistryFleetShard;
import io.apicurio.registry.fleetshard.operator.crd.v1.ApicurioRegistryFleetShardStatus;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.javaoperatorsdk.operator.api.config.informer.InformerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.Cleaner;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.DeleteControl;
import io.javaoperatorsdk.operator.api.reconciler.ErrorStatusHandler;
import io.javaoperatorsdk.operator.api.reconciler.ErrorStatusUpdateControl;
import io.javaoperatorsdk.operator.api.reconciler.EventSourceContext;
import io.javaoperatorsdk.operator.api.reconciler.EventSourceInitializer;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import io.javaoperatorsdk.operator.processing.event.source.EventSource;
import io.javaoperatorsdk.operator.processing.event.source.informer.InformerEventSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@ControllerConfiguration
public class ApicurioRegistryFleetShardReconciler implements
        Reconciler<ApicurioRegistryFleetShard>,
        Cleaner<ApicurioRegistryFleetShard>,
        ErrorStatusHandler<ApicurioRegistryFleetShard>,
        EventSourceInitializer<ApicurioRegistryFleetShard> {

    public static final String SELECTOR = "managed";

    final Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    ControlLoop loop;

    @Override
    public Map<String, EventSource> prepareEventSources(EventSourceContext<ApicurioRegistryFleetShard> context) {
        var deploymentEventSource =
                new InformerEventSource<>(InformerConfiguration.from(Deployment.class, context)
                        .withLabelSelector(SELECTOR)
                        .build(), context);

        return EventSourceInitializer.nameEventSources(deploymentEventSource);
    }

    @Override
    public UpdateControl<ApicurioRegistryFleetShard> reconcile(ApicurioRegistryFleetShard schema, Context<ApicurioRegistryFleetShard> context) {
        // TODO Context handling
        loop.run();

        return UpdateControl.noUpdate();
    }

    @Override
    public DeleteControl cleanup(ApicurioRegistryFleetShard resource, Context<ApicurioRegistryFleetShard> context) {
        return DeleteControl.defaultDelete();
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
