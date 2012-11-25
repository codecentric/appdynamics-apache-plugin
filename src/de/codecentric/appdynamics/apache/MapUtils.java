package de.codecentric.appdynamics.apache;

import java.util.Map;

public final class MapUtils {

    public static String getValue(Map<String, String> args, String key, String defaultValue) {
	String result = args.get(key);
	if (result == null) {
	    return defaultValue;
	} else {
	    return result;
	}
    }

    private MapUtils() {
	// contains static helpers only
    }
}
