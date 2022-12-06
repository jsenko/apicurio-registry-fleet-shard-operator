package io.apicurio.my.ext.it;

import io.apicurio.registry.fleetshard.CRScoped;

import java.util.HashMap;
import java.util.Map;

@CRScoped
public class Data {

    private Map<String, String> data = new HashMap<>();

    public Map<String, String> getData() {
        return data;
    }
}
