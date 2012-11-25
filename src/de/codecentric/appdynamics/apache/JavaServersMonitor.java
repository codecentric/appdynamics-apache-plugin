package de.codecentric.appdynamics.apache;

import java.util.Map;

import org.apache.log4j.Logger;

import com.singularity.ee.agent.systemagent.api.AManagedMonitor;
import com.singularity.ee.agent.systemagent.api.TaskExecutionContext;
import com.singularity.ee.agent.systemagent.api.TaskOutput;
import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;

/**
 * Base class for monitors written in Java.
 */
public abstract class JavaServersMonitor extends AManagedMonitor {
    public static final String SUM = "SUM";
    public static final String AVERAGE = "AVERAGE";

    public static final String OBSERVATION = "OBSERVATION";
    public static final String CURRENT = "CURRENT";
    public static final String COLLECTIVE = "COLLECTIVE";

    private static final Logger logger = Logger.getLogger(JavaServersMonitor.class);

    public abstract TaskOutput execute(Map<String, String> map,
	    TaskExecutionContext taskexecutioncontext) throws TaskExecutionException;

    protected void printStringMetric(String name, String value) {
	String metricName = getMetricPrefix() + name;
	getPropertyWriter().printEnvironmentProperty(metricName, value);
	if (logger.isDebugEnabled())
	    logger.debug("METRIC STRING:  NAME:" + name + " VALUE:" + value);
    }

    protected void printMetric(String name, String value, String aggType, String timeRollup,
	    String clusterRollup) {
	String metricName = getMetricPrefix() + name;
	getMetricWriter(metricName, aggType, timeRollup, clusterRollup).printMetric(value);
	if (logger.isDebugEnabled())
	    logger.debug("METRIC:  NAME:" + metricName + " VALUE:" + value + " :" + aggType + ":"
		    + timeRollup + ":" + clusterRollup);
    }

    protected String getMetricPrefix() {
	return "";
    }

    protected String getString(float num) {
	return Integer.toString(Math.round(num));
    }

    protected String getString(String key, Map<String, String> valueMap) {
	return getString(key, true, valueMap);
    }

    protected String getString(String key, boolean convertUpper, Map<String, String> valueMap) {
	if (convertUpper) {
	    key = key.toUpperCase();
	}
	String strResult = valueMap.get(key);
	if (strResult == null) {
	    return "0";
	}
	float result = Float.valueOf(strResult).floatValue();
	return getString(result);
    }

    protected String getPercent(String numerator, String denumerator, Map<String, String> valueMap) {
	float tmpResult = 0.0F;
	try {
	    tmpResult = getValue(numerator, valueMap) / getValue(denumerator, valueMap);
	} catch (Exception e) {
	    return null;
	}
	tmpResult *= 100F;
	return getString(tmpResult);
    }

    protected String getReversePercent(float numerator, float denumerator) {
	if (denumerator == 0.0F) {
	    return null;
	}
	float tmpResult = numerator / denumerator;
	tmpResult = 1.0F - tmpResult;
	tmpResult *= 100F;
	return getString(tmpResult);
    }

    protected String getPercent(float numerator, float denumerator) {
	if (denumerator == 0.0F) {
	    return getString(0.0F);
	}
	float tmpResult = numerator / denumerator;
	tmpResult *= 100F;
	return getString(tmpResult);
    }

    protected float getValue(String key, Map<String, String> valueMap) {
	String strResult = valueMap.get(key.toUpperCase());
	if (strResult == null) {
	    return 0.0F;
	}
	return Float.valueOf(strResult).floatValue();
    }

    protected float getDiff(String key, Map<String, String> values, Map<String, String> oldValues) {
	String strResult = values.get(key.toUpperCase());
	if (strResult == null) {
	    return 0.0F;
	}
	float result = Float.valueOf(strResult).floatValue();

	float oldResult = 0.0F;
	String oldResultStr = (String) oldValues.get(key.toUpperCase());
	if (oldResultStr != null) {
	    oldResult = Float.valueOf(oldResultStr).floatValue();
	}
	return result - oldResult;
    }
}