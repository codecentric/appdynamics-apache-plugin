package de.codecentric.appdynamics.apache;

import java.util.Map;

import org.apache.log4j.Logger;

import com.singularity.ee.util.httpclient.HttpClientWrapper;
import com.singularity.ee.util.httpclient.HttpExecutionRequest;
import com.singularity.ee.util.httpclient.HttpExecutionResponse;
import com.singularity.ee.util.httpclient.HttpOperation;
import com.singularity.ee.util.httpclient.IHttpClientWrapper;
import com.singularity.ee.util.log4j.Log4JLogger;

/**
 * Reads the mod_status values from the server configured in the configMap passed to readStatus.
 * will try to invoke http://host:port/server-status?auto
 */
public class HttpClientApacheStatusReader implements ApacheStatusReader {

    private static final Logger logger = Logger.getLogger(JavaServersMonitor.class);

    public String readStatus(Map<String, String> configMap) {

	String host = MapUtils.getValue(configMap, "host", "localhost");
	String port = MapUtils.getValue(configMap, "port", "80");

	IHttpClientWrapper httpClient = HttpClientWrapper.getInstance();
	String url = "http://" + host + ":" + port + "/server-status?auto";
	HttpExecutionRequest request = new HttpExecutionRequest(url, "", HttpOperation.GET);
	HttpExecutionResponse response = httpClient.executeHttpOperation(request, new Log4JLogger(
		logger));
	return response.getResponseBody();

    }

}
