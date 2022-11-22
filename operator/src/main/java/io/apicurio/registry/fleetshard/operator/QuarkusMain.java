package io.apicurio.registry.fleetshard.operator;

import io.quarkus.runtime.Quarkus;

@io.quarkus.runtime.annotations.QuarkusMain(name = "QuarkusMain")
public class QuarkusMain {

    public static void main(String... args) {
        Quarkus.run(args);
        Quarkus.waitForExit(); // TODO Is needed?
    }
}
