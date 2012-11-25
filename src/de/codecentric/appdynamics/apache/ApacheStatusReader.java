package de.codecentric.appdynamics.apache;

import java.util.Map;

public interface ApacheStatusReader {

    public String readStatus(Map<String, String> configMap);
}
