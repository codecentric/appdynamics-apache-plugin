package de.codecentric.appdynamics.apache;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Standalone tester which invokes the plugin as the machine agent would do.
 */
public class TestRunner {

    public static void main(String[] args) throws Exception {
	ApacheMonitor apacheMonitor = new ApacheMonitor(new ApacheStatusReaderImplementation(
		"/status/acwn.de"));
	Map<String, String> taskArguments = new HashMap<String, String>();
	Properties p = new Properties();
	p.load(TestRunner.class.getResourceAsStream("/task.properties"));
	apacheMonitor.execute(taskArguments, null);
    }
}
