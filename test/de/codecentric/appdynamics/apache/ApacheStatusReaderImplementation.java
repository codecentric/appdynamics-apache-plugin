package de.codecentric.appdynamics.apache;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.IOUtils;

final class ApacheStatusReaderImplementation implements ApacheStatusReader {
    private String resource;

    public ApacheStatusReaderImplementation(String resource) {
	this.resource = resource;
    }

    public String readStatus(Map<String, String> configMap) {
	try {
	    return IOUtils.toString(TestRunner.class.getResourceAsStream(resource));
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }
}