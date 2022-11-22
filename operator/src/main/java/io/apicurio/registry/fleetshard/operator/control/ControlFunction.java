package io.apicurio.registry.fleetshard.operator.control;

public interface ControlFunction {

    void sense();

    boolean updateNeeded();

    void update();

    boolean cleanupNeeded();

    // TODO Priority
}
