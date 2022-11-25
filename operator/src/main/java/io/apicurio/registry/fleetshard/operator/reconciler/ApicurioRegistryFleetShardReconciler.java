package io.apicurio.registry.fleetshard.operator.reconciler;

import io.apicurio.registry.fleetshard.CRScopeContext;
import io.apicurio.registry.fleetshard.operator.control.ContextHolder;
import io.apicurio.registry.fleetshard.operator.control.ControlLoop;
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

    @Inject
    ContextHolder contextHolder;

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
        var key = schema.getMetadata().getNamespace() + "/" + schema.getMetadata().getName();
        return CRScopeContext.getInstance().with(key, false, () -> {
            contextHolder.setContext(context);
            loop.run();
            return UpdateControl.noUpdate();
        });
    }

    @Override
    public DeleteControl cleanup(ApicurioRegistryFleetShard resource, Context<ApicurioRegistryFleetShard> context) {
        var key = resource.getMetadata().getNamespace() + "/" + resource.getMetadata().getName();
        return CRScopeContext.getInstance().with(key, true, () -> {
            contextHolder.setContext(context);
            loop.runCleanup();
            return DeleteControl.defaultDelete();
        });
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
