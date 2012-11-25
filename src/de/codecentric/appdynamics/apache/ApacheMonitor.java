package de.codecentric.appdynamics.apache;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.tools.ant.util.StringUtils;

import com.singularity.ee.agent.systemagent.api.TaskExecutionContext;
import com.singularity.ee.agent.systemagent.api.TaskOutput;
import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;

/**
 * AppDynamics Machine Agent Monitor for Apache Server monitoring using mod_status.
 */
public class ApacheMonitor extends JavaServersMonitor {

    private static final Pattern colon = Pattern.compile(":");

    private ApacheStatusReader statusReader;

    private volatile Map<String, String> oldValues;

    public ApacheMonitor() {
	this(new HttpClientApacheStatusReader());
    }

    public ApacheMonitor(ApacheStatusReader statusReader) {
	this.statusReader = statusReader;
	oldValues = new HashMap<String, String>();
    }

    public TaskOutput execute(Map<String, String> taskArguments, TaskExecutionContext taskContext)
	    throws TaskExecutionException {
	String statusString = statusReader.readStatus(taskArguments);
	Map<String, String> currentValues = parseStatusString(statusString);
	printMetricsToMetricWriter(currentValues, oldValues);
	oldValues = currentValues;
	return new TaskOutput("Success");
    }

    private void printMetricsToMetricWriter(Map<String, String> values,
	    Map<String, String> oldValues) {
	printMetric("Availability|up", getString(1.0F), OBSERVATION, SUM, COLLECTIVE);
	printMetric("Availability|Server Uptime (sec)", getString("Uptime", values), OBSERVATION,
		CURRENT, "INDIVIDUAL");
	printMetric("Resource Utilization|CPU|Load", getString("CPULoad", values), OBSERVATION,
		AVERAGE, COLLECTIVE);
	printMetric("Resource Utilization|Processes|Busy Workers",
		getString("BusyWorkers", values), OBSERVATION, AVERAGE, COLLECTIVE);
	printMetric("Resource Utilization|Processes|Idle Workers",
		getString("IdleWorkers", values), OBSERVATION, AVERAGE, COLLECTIVE);
	printMetric("Activity|Total Accesses",
		getString(getDiff("Total Accesses", values, oldValues)), OBSERVATION, SUM,
		COLLECTIVE);
	printMetric("Activity|Total Traffic",
		getString(getDiff("Total kBytes", values, oldValues)), OBSERVATION, SUM, COLLECTIVE);
	printMetric("Activity|Requests/min", getString("ReqPerMin", values), OBSERVATION, AVERAGE,
		COLLECTIVE);
	printMetric("Activity|Bytes/min", getString("BytesPerMin", values), OBSERVATION, AVERAGE,
		COLLECTIVE);
	printMetric("Activity|Bytes/req", getString(getDiff("BytesPerReq", values, oldValues)),
		OBSERVATION, AVERAGE, COLLECTIVE);
	printMetric("Activity|Type|Starting Up", getString("StartingUp", values), OBSERVATION,
		CURRENT, COLLECTIVE);
	printMetric("Activity|Type|Reading Request", getString("ReadingRequest", values),
		OBSERVATION, CURRENT, COLLECTIVE);
	printMetric("Activity|Type|Sending Reply", getString("SendingReply", values), OBSERVATION,
		CURRENT, COLLECTIVE);
	printMetric("Activity|Type|Keep Alive", getString("KeepAlive", values), OBSERVATION,
		CURRENT, COLLECTIVE);
	printMetric("Activity|Type|DNS Lookup", getString("DNSLookup", values), OBSERVATION,
		CURRENT, COLLECTIVE);
	printMetric("Activity|Type|Closing Connection", getString("ClosingConnection", values),
		OBSERVATION, CURRENT, COLLECTIVE);
	printMetric("Activity|Type|Logging", getString("Logging", values), OBSERVATION, CURRENT,
		COLLECTIVE);
	printMetric("Activity|Type|Gracefully Finishing", getString("GracefullyFinishing", values),
		OBSERVATION, CURRENT, COLLECTIVE);
	printMetric("Activity|Type|Cleaning Up", getString("CleaningUp", values), OBSERVATION,
		CURRENT, COLLECTIVE);
    }

    protected Map<String, String> parseStatusString(String statStr) {
	Map<String, String> values = new HashMap<String, String>();

	@SuppressWarnings(value = "unchecked")
	// we use a bundled old version of StringUtils to split string
	List<String> lines = StringUtils.lineSplit(statStr);
	for (String line : lines) {
	    String result[] = colon.split(line);
	    if (result.length == 2) {
		String key = result[0];
		String value = result[1];
		if (key.equals("Scoreboard")) {
		    Scoreboard scores = new Scoreboard(value);
		    values.put("STARTINGUP", Integer.toString(scores.StartingUp));
		    values.put("READINGREQUEST", Integer.toString(scores.ReadingRequest));
		    values.put("SENDINGREPLY", Integer.toString(scores.SendingReply));
		    values.put("KEEPALIVE", Integer.toString(scores.KeepAlive));
		    values.put("DNSLOOKUP", Integer.toString(scores.DNSLookup));
		    values.put("CLOSINGCONNECTION", Integer.toString(scores.ClosingConnection));
		    values.put("LOGGING", Integer.toString(scores.Logging));
		    values.put("GRACEFULLYFINISHING", Integer.toString(scores.GracefullyFinishing));
		    values.put("CLEANINGUP", Integer.toString(scores.CleaningUp));
		} else {
		    if (key.equals("BytesPerSec")) {
			key = "BytesPerMin";
			float floatVal = Float.valueOf(value).floatValue();
			value = getString(floatVal * 60F);
		    }
		    if (key.equals("ReqPerSec")) {
			key = "ReqPerMin";
			float floatVal = Float.valueOf(value).floatValue();
			value = getString(floatVal * 60F);
		    }
		    values.put(key.toUpperCase(), value);
		}
	    }
	}
	return Collections.synchronizedMap(values);
    }

    protected String getMetricPrefix() {
	return "Custom Metrics|WebServer|Apache|Status|";
    }

}
