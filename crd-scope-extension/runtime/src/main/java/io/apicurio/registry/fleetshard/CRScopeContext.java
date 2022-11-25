package io.apicurio.registry.fleetshard;

import io.quarkus.arc.ContextInstanceHandle;
import io.quarkus.arc.InjectableBean;
import io.quarkus.arc.InjectableContext;
import io.quarkus.arc.impl.ContextInstanceHandleImpl;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;

public class CRScopeContext implements InjectableContext { // InjectableContext extends AlterableContext extends Context

    private final Map<String, Scope> SCOPES = new ConcurrentHashMap<>();

    static final ThreadLocal<String> ACTIVE_SCOPE_KEY_ON_THREAD = new ThreadLocal<>();

    @Override
    public Class<? extends Annotation> getScope() {
        return CRScoped.class;
    }

    @Override
    public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
        Map<Contextual<?>, ContextInstanceHandle<?>> activeScope = getActiveScope().orElseThrow(ContextNotActiveException::new).getInstances();

        @SuppressWarnings("unchecked")
        ContextInstanceHandle<T> contextInstanceHandle = (ContextInstanceHandle<T>) activeScope
                .computeIfAbsent(contextual, c -> {
                    if (creationalContext == null) {
                        return null;
                    }
                    T createdInstance = contextual.create(creationalContext);
                    return new ContextInstanceHandleImpl<T>((InjectableBean<T>) contextual, createdInstance,
                            creationalContext);
                });

        return contextInstanceHandle.get();
    }

    @Override
    public <T> T get(Contextual<T> contextual) {
        Map<Contextual<?>, ContextInstanceHandle<?>> activeScope = getActiveScope().orElseThrow(ContextNotActiveException::new).getInstances();

        @SuppressWarnings("unchecked")
        ContextInstanceHandle<T> contextInstanceHandle = (ContextInstanceHandle<T>) activeScope.get(contextual);

        if (contextInstanceHandle == null) {
            return null;
        }

        return contextInstanceHandle.get();
    }

    @Override
    public boolean isActive() {
        return getActiveScope().isPresent();
    }

    @Override
    public void destroy(Contextual<?> contextual) {
        getActiveScope().map(scope -> scope.getInstances().get(contextual))
                .ifPresent(ContextInstanceHandle::destroy);
    }

    @Override
    public void destroy() {
        var activeScope = getActiveScope();
        activeScope.ifPresent(scope -> scope.getInstances().values().forEach(ContextInstanceHandle::destroy));
        activeScope.orElseThrow(ContextNotActiveException::new);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ContextState getState() {
        return () -> getActiveScope().map(scope ->
                scope.getInstances()
                        .values()
                        .stream()
                        .collect(Collectors.toMap(ContextInstanceHandle::getBean, ContextInstanceHandle::get))
        ).orElse((Map) Collections.emptyMap());
    }

    /**
     * Own API
     */
    private final static CRScopeContext INSTANCE = new CRScopeContext();

    public static CRScopeContext getInstance() {
        return INSTANCE;
    }

    public Optional<Scope> getActiveScope() {
        return Optional.ofNullable(ACTIVE_SCOPE_KEY_ON_THREAD.get())
                .map(SCOPES::get);
    }

    public void enter(String key) {
        var activeScope = getActiveScope();

        if (activeScope.isPresent()) {
            throw new IllegalStateException("An instance of this scope is already active");
        }

        var newScope = new Scope();
        SCOPES.put(key, newScope);
        ACTIVE_SCOPE_KEY_ON_THREAD.set(key);
    }

    public void exit(boolean delete) {
        var activeScope = getActiveScope();

        if (activeScope.isEmpty()) {
            throw new IllegalStateException("Scope currently not active");
        }

        if (delete) {
            SCOPES.remove(ACTIVE_SCOPE_KEY_ON_THREAD.get());
        }
        ACTIVE_SCOPE_KEY_ON_THREAD.set(null);
    }

    public <R> R with(String key, boolean delete, Supplier<R> f) {
        try {
            this.enter(key);
            return f.get();
        } finally {
            this.exit(delete);
        }
    }

    public static class Scope {

        private Map<Contextual<?>, ContextInstanceHandle<?>> instances = new ConcurrentHashMap<>();

        public Map<Contextual<?>, ContextInstanceHandle<?>> getInstances() {
            return instances;
        }

        public void setInstances(Map<Contextual<?>, ContextInstanceHandle<?>> instances) {
            this.instances = instances;
        }
    }
}
