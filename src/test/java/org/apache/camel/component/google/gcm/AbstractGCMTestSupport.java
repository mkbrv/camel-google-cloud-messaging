package org.apache.camel.component.google.gcm;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.component.google.gcm.configuration.GCMConfiguration;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.util.IntrospectionSupport;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by miki on 10.04.2015.
 */
public class AbstractGCMTestSupport extends CamelTestSupport {

    private static final String TEST_OPTIONS_PROPERTIES = "/test-options.properties";
    protected Properties testProperties;

    @Override
    protected CamelContext createCamelContext() throws Exception {
        final CamelContext context = super.createCamelContext();
        testProperties = readProperties(TEST_OPTIONS_PROPERTIES);


        Map<String, Object> options = new HashMap<String, Object>();
        for (Map.Entry<Object, Object> entry : testProperties.entrySet()) {
            options.put(entry.getKey().toString(), entry.getValue());
        }

        final GCMConfiguration config = new GCMConfiguration();
        IntrospectionSupport.setProperties(config, options);

        final GCMComponent component = new GCMComponent(context);
        context.addComponent("google-cloud-messaging", component);


        return context;
    }

    private Properties readProperties(final String source) throws IOException {
        // read GCM component configuration from TEST_OPTIONS_PROPERTIES
        final Properties properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream(source));
        } catch (Exception e) {
            throw new IOException(String.format("%s could not be loaded: %s", source, e.getMessage()), e);
        }
        return properties;
    }

    @Override
    public boolean isCreateCamelContextPerClass() {
        // only create the context once for this class
        return true;
    }

    @SuppressWarnings("unchecked")
    protected <T> T requestBodyAndHeaders(String endpointUri, Object body, Map<String, Object> headers) throws CamelExecutionException {
        return (T) template().requestBodyAndHeaders(endpointUri, body, headers);
    }

    @SuppressWarnings("unchecked")
    protected <T> T requestBody(String endpoint, Object body) throws CamelExecutionException {
        return (T) template().requestBody(endpoint, body);
    }
}
